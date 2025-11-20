package com.android.streaming.presentation.main.engine

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.streaming.presentation.main.model.CameraFacing
import com.haishinkit.media.MediaMixer
import com.haishinkit.media.source.AudioRecordSource
import com.haishinkit.media.source.Camera2Source
import com.haishinkit.rtmp.RtmpConnection
import com.haishinkit.rtmp.RtmpStream
import com.haishinkit.rtmp.RtmpStreamSessionFactory
import com.haishinkit.rtmp.event.Event
import com.haishinkit.rtmp.event.EventUtils
import com.haishinkit.rtmp.event.IEventListener
import com.haishinkit.stream.StreamSession
import com.haishinkit.view.HkSurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import androidx.core.net.toUri

class StreamEngine(
    context: Context,
    private val onEvent: (StreamEngineEvent) -> Unit
) : DefaultLifecycleObserver {

    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val cameraManager = appContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val mediaMixer = MediaMixer(appContext)
    private val engineJob = SupervisorJob()
    private val scope = CoroutineScope(engineJob + Dispatchers.Main.immediate)
    private val sourceMutex = Mutex()
    private var previewView: HkSurfaceView? = null
    private var currentFacing: CameraFacing = CameraFacing.BACK
    private var micEnabled: Boolean = true
    private var cameraSource: Camera2Source? = null
    private var audioSource: AudioRecordSource? = null
    private var streamSession: StreamSession? = null
    private val rtmpStatusListener =
        object : IEventListener {
            override fun handleEvent(event: Event) {
                val data = EventUtils.toMap(event)
                val code = data["code"]?.toString().orEmpty()
                when (code) {
                    RtmpConnection.Code.CONNECT_CLOSED.rawValue -> postEvent(StreamEngineEvent.Disconnected)
                    RtmpConnection.Code.CONNECT_FAILED.rawValue,
                    RtmpConnection.Code.CONNECT_REJECTED.rawValue,
                    RtmpStream.Code.PUBLISH_BAD_NAME.rawValue -> {
                        val message = data["description"]?.toString() ?: code
                        postEvent(StreamEngineEvent.Failed(message))
                    }
                }
            }
        }

    init {
        StreamSession.Builder.registerFactory(RtmpStreamSessionFactory)
    }

    fun getPreviewView(): HkSurfaceView {
        val existing = previewView
        if (existing != null) {
            return existing
        }
        val view = HkSurfaceView(appContext).apply { keepScreenOn = true }
        mediaMixer.registerOutput(view)
        previewView = view
        return view
    }

    fun startPreview(facing: CameraFacing) {
        scope.launch(Dispatchers.Default) {
            sourceMutex.withLock {
                ensureVideoSource(facing)
                ensureAudioState()
            }
        }
    }

    fun startStream(streamKey: String) {
        val url = resolveEndpoint(streamKey)
        scope.launch(Dispatchers.Default) {
            if (streamSession != null) return@launch
            if (url.isBlank()) {
                postEvent(StreamEngineEvent.Failed("Stream url is empty"))
                return@launch
            }
            sourceMutex.withLock {
                ensureVideoSource(currentFacing)
                ensureAudioState()
            }
            val uri = runCatching { url.toUri() }.getOrElse {
                postEvent(StreamEngineEvent.Failed("Invalid stream url"))
                return@launch
            }
            val session = runCatching { StreamSession.Builder(appContext, uri).build() }.getOrElse {
                postEvent(StreamEngineEvent.Failed(it.message ?: "Unable to create stream session"))
                return@launch
            }
            val stream = session.stream
            mediaMixer.registerOutput(stream)
            (stream as? RtmpStream)?.let { rtmp ->
                configureStream(rtmp)
                rtmp.addEventListener(Event.RTMP_STATUS, rtmpStatusListener)
            }
            streamSession = session
            val result = session.connect(StreamSession.Method.INGEST)
            if (result.isSuccess) {
                postEvent(StreamEngineEvent.Connected)
            } else {
                val message = result.exceptionOrNull()?.message ?: "Unable to start stream"
                postEvent(StreamEngineEvent.Failed(message))
                closeSession(session)
            }
        }
    }

    fun stopStream() {
        scope.launch(Dispatchers.Default) {
            closeSession(streamSession)
            postEvent(StreamEngineEvent.Disconnected)
        }
    }

    fun setMicEnabled(enabled: Boolean) {
        micEnabled = enabled
        scope.launch(Dispatchers.Default) {
            sourceMutex.withLock {
                ensureAudioState()
            }
        }
    }

    fun switchCamera() {
        val target = currentFacing.toggle()
        startPreview(target)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        release()
    }

    fun release() {
        val cleanupJob =
            scope.launch(Dispatchers.Default) {
                closeSession(streamSession)
                sourceMutex.withLock {
                    detachVideoSource()
                    detachAudioSource()
                }
                previewView?.let { mediaMixer.unregisterOutput(it) }
                previewView = null
                mediaMixer.dispose()
            }
        cleanupJob.invokeOnCompletion {
            engineJob.cancel()
        }
    }

    private suspend fun ensureVideoSource(facing: CameraFacing) {
        val targetId = resolveCameraId(facing)
        if (targetId == null) {
            postEvent(StreamEngineEvent.Failed("Camera not available"))
            return
        }
        if (cameraSource?.cameraId == targetId) {
            currentFacing = facing
            return
        }
        detachVideoSource()
        val newSource = Camera2Source(appContext, targetId)
        val result = mediaMixer.attachVideo(VIDEO_TRACK, newSource)
        if (result.isSuccess) {
            cameraSource = newSource
            currentFacing = facing
        } else {
            postEvent(StreamEngineEvent.Failed(result.exceptionOrNull()?.message ?: "Unable to start camera"))
        }
    }

    private suspend fun ensureAudioState() {
        if (micEnabled) {
            if (audioSource == null) {
                val newSource = AudioRecordSource(appContext)
                val result = mediaMixer.attachAudio(AUDIO_TRACK, newSource)
                if (result.isSuccess) {
                    audioSource = newSource
                } else {
                    postEvent(StreamEngineEvent.Failed(result.exceptionOrNull()?.message ?: "Unable to start microphone"))
                }
            }
        } else {
            detachAudioSource()
        }
    }

    private suspend fun detachVideoSource() {
        cameraSource?.let {
            mediaMixer.attachVideo(VIDEO_TRACK, null)
            cameraSource = null
        }
    }

    private suspend fun detachAudioSource() {
        audioSource?.let {
            mediaMixer.attachAudio(AUDIO_TRACK, null)
            audioSource = null
        }
    }

    private suspend fun closeSession(session: StreamSession?) {
        val current = session ?: return
        val stream = current.stream
        (stream as? RtmpStream)?.removeEventListener(Event.RTMP_STATUS, rtmpStatusListener)
        mediaMixer.unregisterOutput(stream)
        runCatching { current.close() }
        if (streamSession == current) {
            streamSession = null
        }
    }

    private fun configureStream(stream: RtmpStream) {
        stream.videoSetting.apply {
            width = DEFAULT_WIDTH
            height = DEFAULT_HEIGHT
            bitRate = DEFAULT_VIDEO_BITRATE
            frameRate = DEFAULT_FRAME_RATE
            IFrameInterval = DEFAULT_I_FRAME_INTERVAL
        }
        stream.audioSetting.apply {
            bitRate = DEFAULT_AUDIO_BITRATE
        }
    }

    private fun resolveCameraId(facing: CameraFacing): String? =
        runCatching {
            val desired =
                if (facing == CameraFacing.FRONT) {
                    CameraCharacteristics.LENS_FACING_FRONT
                } else {
                    CameraCharacteristics.LENS_FACING_BACK
                }
            cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING) == desired
            } ?: cameraManager.cameraIdList.firstOrNull()
        }.getOrNull()

    private fun postEvent(event: StreamEngineEvent) {
        mainHandler.post { onEvent(event) }
    }
    private fun resolveEndpoint(streamKeyOrUrl: String): String {
        return if (streamKeyOrUrl.startsWith("rtmp://")) {
            streamKeyOrUrl
        } else {
            "rtmp://live.twitch.tv/app/$streamKeyOrUrl"
        }
    }
    companion object {
        private const val VIDEO_TRACK = 0
        private const val AUDIO_TRACK = 0
        private const val DEFAULT_WIDTH = 1280
        private const val DEFAULT_HEIGHT = 720
        private const val DEFAULT_FRAME_RATE = 30
        private const val DEFAULT_I_FRAME_INTERVAL = 2
        private const val DEFAULT_VIDEO_BITRATE = 2_500_000
        private const val DEFAULT_AUDIO_BITRATE = 128_000
    }
}


package com.android.tar

import android.app.Application
import com.android.database.impl.di.StreamViewerDatabase
import com.android.datastore.impl.di.DataStoreModule
import com.android.dispatchers.provider.di.DispatchersModule
import com.android.network.impl.di.DataModule
import com.android.streaming.di.StreamModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(modules)
        }
    }
    private val modules = listOf(
        AppModule().module,
        DataModule().module,
        DispatchersModule().module,
        DataStoreModule().module,
        StreamViewerDatabase().module,
        StreamModule().module,
    )
}
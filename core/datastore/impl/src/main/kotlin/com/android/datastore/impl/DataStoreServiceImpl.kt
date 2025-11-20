package com.android.datastore.impl

import android.content.*

import com.android.utils.DEFAULT_BOOLEAN
import com.android.utils.DEFAULT_FLOAT
import com.android.utils.DEFAULT_INT
import com.android.utils.DEFAULT_LONG
import com.android.utils.EMPTY_STRING
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class DataStoreServiceImpl(context: Context) : DataStoreService {

    private val sharedPrefs = context.getSharedPreferences("info", Context.MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    private val savedStreamKey = "savedStreamKey"


    private fun set(key: String, value: Any?) {
        when (value) {
            is Int -> editor.putInt(key, value).apply()
            is Long -> editor.putLong(key, value).apply()
            is Float -> editor.putFloat(key, value).apply()
            is String -> editor.putString(key, value).apply()
            is Boolean -> editor.putBoolean(key, value).apply()
            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    private inline operator fun <reified T> get(
        key: String
    ): T {
        return when (T::class) {
            Int::class -> sharedPrefs.getInt(key, DEFAULT_INT) as T
            Long::class -> sharedPrefs.getLong(key, DEFAULT_LONG) as T
            Float::class -> sharedPrefs.getFloat(key, DEFAULT_FLOAT) as T
            String::class -> sharedPrefs.getString(key, EMPTY_STRING).orEmpty() as T
            Boolean::class -> sharedPrefs.getBoolean(key, DEFAULT_BOOLEAN) as T
            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    override var streamKey: String
        get() = get(savedStreamKey)
        set(value) {
            set(savedStreamKey, value)
        }

}

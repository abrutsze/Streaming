package com.android.database.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.database.api.ViewerDao
import com.android.database.api.ViewerEntity

@Database(
    entities = [ViewerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ViewerDatabase : RoomDatabase() {

    abstract fun viewerDao(): ViewerDao

    companion object {
        const val NAME = "viewer-db"
    }
}


package com.android.database.impl.di

import android.content.Context
import androidx.room.Room
import com.android.database.impl.ViewerDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.android.database.impl")
class StreamViewerDatabase {
    @Single
    fun provideViewerDatabase(context: Context): ViewerDatabase {
        return Room.databaseBuilder(
            context,
            ViewerDatabase::class.java,
            ViewerDatabase.NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Single
    fun provideViewerDao(database: ViewerDatabase) = database.viewerDao()
}
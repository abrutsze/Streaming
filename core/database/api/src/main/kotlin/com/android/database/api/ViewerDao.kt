package com.android.database.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ViewerDao {

    @Query("SELECT * FROM viewers ORDER BY fullName ASC")
    fun observeViewers(): Flow<List<ViewerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(viewers: List<ViewerEntity>)

    @Query("DELETE FROM viewers")
    suspend fun clear()
}


package com.android.database.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewers")
data class ViewerEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val username: String,
    val avatarUrl: String,
    val location: String
)


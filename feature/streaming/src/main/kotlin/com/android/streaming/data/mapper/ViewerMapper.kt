package com.android.streaming.data.mapper

import com.android.database.api.ViewerEntity
import com.android.models.UiViewer
import com.android.response.RandomUserDto

internal fun RandomUserDto.toEntity(): ViewerEntity {
    val locationLabel = listOf(location.city, location.state, location.country)
        .filter { it.isNotBlank() }
        .joinToString(", ")
    return ViewerEntity(
        id = login.uuid,
        fullName = listOf(name.first, name.last).joinToString(" ").trim(),
        username = login.username,
        avatarUrl = picture.medium.ifBlank { picture.thumbnail },
        location = locationLabel
    )
}

internal fun ViewerEntity.toUiModel(): UiViewer {
    return UiViewer(
        id = id,
        name = fullName,
        username = username,
        avatarUrl = avatarUrl,
        location = location
    )
}


package com.android.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomUserResponse(
    @SerialName("results") val results: List<RandomUserDto> = emptyList()
)

@Serializable
data class RandomUserDto(
    @SerialName("login") val login: LoginDto,
    @SerialName("name") val name: NameDto,
    @SerialName("picture") val picture: PictureDto,
    @SerialName("location") val location: LocationDto
)

@Serializable
data class LoginDto(
    @SerialName("uuid") val uuid: String,
    @SerialName("username") val username: String
)

@Serializable
data class NameDto(
    @SerialName("first") val first: String,
    @SerialName("last") val last: String
)

@Serializable
data class PictureDto(
    @SerialName("medium") val medium: String,
    @SerialName("thumbnail") val thumbnail: String
)

@Serializable
data class LocationDto(
    @SerialName("country") val country: String = "",
    @SerialName("city") val city: String = "",
    @SerialName("state") val state: String = ""
)


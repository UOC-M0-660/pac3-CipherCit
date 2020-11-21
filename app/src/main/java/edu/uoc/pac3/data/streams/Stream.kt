package edu.uoc.pac3.data.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class Stream(
    @SerialName("thumbnail_url") val imageUri: String? = null,
    @SerialName("user_name") val userName: String? = null,
    val title: String? = null,
)

@Serializable
data class StreamsResponse(
    val data: List<Stream>? = null,
    val pagination: Pagination? = null
)

@Serializable
data class Pagination(val cursor: String? = null)
package edu.uoc.pac3.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class User(
    @SerialName("display_name") val userName: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("profile_image_url") val profileImage: String? = null,
    @SerialName("view_count") val viewsCount: Long? = null
)

@Serializable
data class UserResponse(
    val data: List<User>? = null
)
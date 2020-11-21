package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {

    const val clientId = "hcxrwbciex1jvenhqd9nq6cdvm3kuu"
    const val clientSecret = "ky2fdxf34kv293e3fxumv3c23wpt6s"
    const val redirectHost = "localhost"
    const val redirectUri = "http://$redirectHost"
    const val responseType = "code"
    const val scope = "user:read:email+user:edit+user:edit:follows+channel:read:stream_key"
}
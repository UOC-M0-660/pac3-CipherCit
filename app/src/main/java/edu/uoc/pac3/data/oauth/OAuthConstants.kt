package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {
    // Carlos client ids
    //const val clientId = "hcxrwbciex1jvenhqd9nq6cdvm3kuu"
    //const val clientSecret = "ky2fdxf34kv293e3fxumv3c23wpt6s"

    // Test client ids
    const val clientId = "efwo35z4mgyiyhje8bbp73b98oyavf"
    const val clientSecret = "7fl44yqjm5tjdx73z45dd9ybwuuiez"

    const val redirectHost = "localhost"
    const val redirectUri = "http://$redirectHost"
    const val responseType = "code"
    const val scope = "user:read:email+user:edit+user:edit:follows+channel:read:stream_key"
}
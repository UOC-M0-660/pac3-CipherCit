package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"
    const val oauthAuthorizeUrl = "$oauthBaseUrl/authorize"
    const val oauthTokenUrl = "$oauthBaseUrl/token"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"
    const val twitchStreamsUrl = "$twitchBaseUrl/streams"
}
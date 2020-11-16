package edu.uoc.pac3.data

import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? =
        try {
            val tokensResponse = httpClient.post<OAuthTokensResponse>(Endpoints.oauthTokenUrl) {
                parameter("client_id", OAuthConstants.clientId)
                parameter("client_secret", OAuthConstants.clientSecret)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", OAuthConstants.redirectUri)
            }

            tokensResponse
        } catch (t: Throwable) {
            null
        }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(authToken: String? = null, cursor: String? = null): StreamsResponse? {

        if (authToken == null) return null

        return try {
            val streamsResponse = httpClient.get<StreamsResponse>(Endpoints.twitchStreamsUrl) {
                header("Authorization", "Bearer $authToken")
                header("Client-Id", OAuthConstants.clientId)
            }
            streamsResponse
        }catch (t: Throwable) {
            throw UnauthorizedException
        }

        // TODO("Support Pagination")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        TODO("Get User from Twitch")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        TODO("Update User Description on Twitch")
    }
}
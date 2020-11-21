package edu.uoc.pac3.data

import edu.uoc.pac3.MyApp
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.UserResponse
import io.ktor.client.*
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
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        val context = MyApp.getContext() ?: return null

        val sessionManager = SessionManager(context)

        val authToken = sessionManager.getAccessToken()
        val refreshToken = sessionManager.getRefreshToken()

        if (authToken == null || refreshToken == null) return null

        return try {
            requestStreamsToApi(cursor, authToken)
        }catch (t: Throwable) {
            when(t) {
                is ClientRequestException -> {
                    if (t.response?.status?.value == 401) {
                        // refresh token and try again
                        if (updateAccessToken(refreshToken)){
                            try {
                                return requestStreamsToApi(cursor, sessionManager.getAccessToken()!!)
                            }catch (t: Throwable){
                                throw UnauthorizedException
                            }
                        }
                    }
                }
                else -> {
                    throw UnauthorizedException
                }
            }
            null
        }
    }

    @Throws(UnauthorizedException::class)
    private suspend fun requestStreamsToApi(cursor: String? = null, token: String): StreamsResponse {
        try {
            return httpClient.get(Endpoints.twitchStreamsUrl) {
                if (cursor != null) {
                    parameter("after", cursor)
                }
                header("Authorization", "Bearer $token")
                header("Client-Id", OAuthConstants.clientId)
            }
        }catch (t: Throwable) {
            throw t
        }
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
        val context = MyApp.getContext() ?: return null

        val sessionManager = SessionManager(context)

        val authToken = sessionManager.getAccessToken() ?: return null

        return try {
            val userResponse = httpClient.get<UserResponse>(Endpoints.twitchUserUrl) {
                header("Authorization", "Bearer $authToken")
                header("Client-Id", OAuthConstants.clientId)
            }
           userResponse.data?.firstOrNull()
        }catch (t: Throwable) {
            throw UnauthorizedException
        }
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        val context = MyApp.getContext() ?: return null

        val sessionManager = SessionManager(context)

        val authToken = sessionManager.getAccessToken() ?: return null

        return try {
            val userResponse = httpClient.put<UserResponse>(Endpoints.twitchUserUrl) {
                parameter("description", description)
                header("Authorization", "Bearer $authToken")
                header("Client-Id", OAuthConstants.clientId)
            }
            userResponse.data?.firstOrNull()
        }catch (t: Throwable) {
            throw UnauthorizedException
        }
    }

    /// Handles 401 error and update access token with refresh token
    /// Return true if updates access token
    private suspend fun updateAccessToken(refreshToken: String): Boolean {
        return try {
            val newTokensResponse = httpClient.post<OAuthTokensResponse>(Endpoints.oauthTokenUrl) {
                parameter("grant_type", "refresh_token")
                parameter("refresh_token", refreshToken)
                parameter("client_id", OAuthConstants.clientId)
                parameter("client_secret", OAuthConstants.clientSecret)
            }

            val context = MyApp.getContext() ?: return false
            val sessionManager = SessionManager(context)
            sessionManager.saveAccessToken(newTokensResponse.accessToken)
            sessionManager.saveRefreshToken(newTokensResponse.refreshToken ?: "")
            true

        }catch (t: Throwable) {
            false
        }
    }
}
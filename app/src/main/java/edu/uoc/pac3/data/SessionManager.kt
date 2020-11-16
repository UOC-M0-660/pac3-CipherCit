package edu.uoc.pac3.data

import android.content.Context

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    private var sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    private val accessTokenKey = "accessToken"
    private val refreshTokenKey = "refreshToken"

    fun isUserAvailable(): Boolean = getAccessToken() != null

    fun getAccessToken(): String? = sharedPreferences.getString(accessTokenKey, null)

    fun saveAccessToken(accessToken: String) {
        with(sharedPreferences.edit()) {
            putString(accessTokenKey, accessToken)
            apply()
        }
    }

    fun clearAccessToken() {
        with(sharedPreferences.edit()){
            putString(accessTokenKey, null)
            apply()
        }
    }

    fun getRefreshToken(): String? = sharedPreferences.getString(refreshTokenKey, null)

    fun saveRefreshToken(refreshToken: String) {
        with(sharedPreferences.edit()){
            putString(refreshTokenKey, refreshToken)
            apply()
        }
    }

    fun clearRefreshToken() {
        with(sharedPreferences.edit()){
            putString(refreshTokenKey, null)
            apply()
        }
    }
}
package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    fun buildOAuthUri(): Uri {
        val endpoint = Endpoints.oauthAuthorizeUrl +
            "?client_id=${OAuthConstants.clientId}" +
            "&redirect_uri=${OAuthConstants.redirectUri}" +
            "&response_type=${OAuthConstants.responseType}" +
            "&scope=${OAuthConstants.scope}"
        return Uri.parse(endpoint)
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (request != null) {
                    if(request.url.host != OAuthConstants.redirectHost) return false

                    // Get token
                    val authorizationCode = request.url.getQueryParameter("code")
                    if (authorizationCode != null) {
                        onAuthorizationCodeRetrieved(authorizationCode)
                        return true
                    }
                }

                return false
            }
        }

        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {
        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE

        val httpClient = Network.createHttpClient(this)
        val twitchService = TwitchApiService(httpClient)

        lifecycleScope.launch(Dispatchers.IO){
            val tokensResponse = twitchService.getTokens(authorizationCode)

            if (tokensResponse != null) {
                val sessionManager = SessionManager(applicationContext)
                // Save tokens
                sessionManager.saveAccessToken(tokensResponse.accessToken)
                sessionManager.saveRefreshToken(tokensResponse.refreshToken ?: "")
                startActivity(Intent(this@OAuthActivity, StreamsActivity::class.java))
                finish()
            }
        }
    }
}
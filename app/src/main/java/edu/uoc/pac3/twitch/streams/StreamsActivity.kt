package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()

        val httpClient = Network.createHttpClient(this)
        val twitchService = TwitchApiService(httpClient)

        Dispatchers.IO.run {
            runBlocking {
                try {
                    val streamsResponse = twitchService.getStreams(SessionManager(this@StreamsActivity).getAccessToken()!!)

                    if (streamsResponse != null) {
                        (findViewById<RecyclerView>(R.id.recyclerView).adapter as StreamsAdapter).setStreams(streamsResponse.data)
                    }
                }catch (e: UnauthorizedException) {
                    // ??
                }
            }
        }
    }

    private fun initRecyclerView() {
        findViewById<RecyclerView>(R.id.recyclerView).adapter = StreamsAdapter(listOf())
    }

}
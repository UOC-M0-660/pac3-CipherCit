package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"
    private var paginationCursor: String? = null
    private var twitchService: TwitchApiService? = null
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()

        val httpClient = Network.createHttpClient(this)
        twitchService = TwitchApiService(httpClient)

        loadStreams(null)
    }

    private fun loadStreams(withCursor: String?) {
        if (isLoading) return

        isLoading = true
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val streamsResponse = twitchService!!.getStreams(withCursor, SessionManager(this@StreamsActivity).getAccessToken()!!)

                if (streamsResponse != null) {
                    paginationCursor = streamsResponse.pagination?.cursor
                    runOnUiThread {
                        (findViewById<RecyclerView>(R.id.recyclerView).adapter as StreamsAdapter).addStreams(streamsResponse.data)
                    }
                }

                isLoading = false
            }catch (e: UnauthorizedException) {
                isLoading = false
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = StreamsAdapter(listOf())

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if (layoutManager.findLastVisibleItemPosition() + 2 > layoutManager.itemCount) {
                    loadStreams(paginationCursor)
                }
            }
        })
    }

}
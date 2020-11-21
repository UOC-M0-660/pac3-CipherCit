package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.twitch.profile.ProfileActivity
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
                val streamsResponse = twitchService!!.getStreams(withCursor)

                if (streamsResponse != null) {
                    paginationCursor = streamsResponse.pagination?.cursor
                    runOnUiThread {
                        (findViewById<RecyclerView>(R.id.recyclerView).adapter as StreamsAdapter).addStreams(streamsResponse.data)
                    }
                } else {
                    startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
                    this@StreamsActivity.finish()
                }

                isLoading = false
            }catch (e: UnauthorizedException) {
                isLoading = false
                // Error loading streams, back to Login Activity
                startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
                this@StreamsActivity.finish()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.streams_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // we have only one option
        startActivity(Intent(this, ProfileActivity::class.java))
        return true
    }
}
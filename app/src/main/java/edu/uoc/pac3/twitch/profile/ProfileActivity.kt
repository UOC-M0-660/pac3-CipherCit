package edu.uoc.pac3.twitch.profile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val httpClient = Network.createHttpClient(this)
        val twitchService = TwitchApiService(httpClient)

        lifecycleScope.launch(Dispatchers.IO){
            val user = twitchService.getUser()

            if(user != null) {
                updateUI(user)
            } else{
                runOnUiThread {
                    Toast.makeText(this@ProfileActivity, "There were an error getting your user info", Toast.LENGTH_LONG).show()
                }
            }
        }

        findViewById<MaterialButton>(R.id.updateDescriptionButton).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val newDescription = findViewById<TextInputEditText>(R.id.userDescriptionEditText).text.toString()
                val user = twitchService.updateUserDescription(newDescription)

                if(user != null) {
                    updateUI(user)
                    runOnUiThread {
                        Toast.makeText(this@ProfileActivity, "Done!", Toast.LENGTH_LONG).show()
                    }
                } else{
                    runOnUiThread {
                        Toast.makeText(this@ProfileActivity, "There were an updating your info", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            SessionManager(this).clearAccessToken()
            SessionManager(this).clearRefreshToken()
        }
    }

    private fun updateUI(user: User){
        runOnUiThread {
            val imageView = findViewById<ImageView>(R.id.imageView)
            Glide.with(this@ProfileActivity)
                .load(user.profileImage)
                .centerCrop()
                .into(imageView)

            findViewById<TextView>(R.id.viewsText).text = "${user.viewsCount} Views"
            findViewById<TextView>(R.id.userNameTextView).text = user.userName
            findViewById<TextInputEditText>(R.id.userDescriptionEditText).setText(user.description)
        }
    }
}
package edu.uoc.pac3

import android.app.Application
import android.content.Context

class MyApp: Application() {

    companion object {
        private var context: Context? = null

        fun getContext(): Context? {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
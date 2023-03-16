package com.muhamapps.githubuserapp3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.muhamapps.githubuserapp3.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this,
                MainActivity::class.java))
            finish()
        }, 4000)
    }
}
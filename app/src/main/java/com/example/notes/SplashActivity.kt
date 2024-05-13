package com.example.notes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Handler


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Using Handler to delay the start of MainActivity
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Optional: Finish the SplashActivity to prevent going back to it when pressing back button
        }, SPLASH_DELAY)
    }

    companion object {
        private const val SPLASH_DELAY: Long = 2000 // Splash screen delay time in milliseconds (2 seconds)
    }
}
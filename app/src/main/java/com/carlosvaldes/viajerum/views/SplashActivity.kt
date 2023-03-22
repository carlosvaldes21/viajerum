package com.carlosvaldes.viajerum.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.R
import com.carlosvaldes.viajerum.views.auth.AuthActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            finish()
        }, 1500)
    }
}
package com.practice.acronym.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.practice.acronym.R
import com.practice.acronym.domain_layer.base.IConstant.SPLASH_SECONDS
import java.util.concurrent.TimeUnit

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, TimeUnit.SECONDS.toMillis(SPLASH_SECONDS))
    }
}

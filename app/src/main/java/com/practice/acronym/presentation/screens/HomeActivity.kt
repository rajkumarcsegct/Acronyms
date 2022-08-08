package com.practice.acronym.presentation.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.acronym.R
import com.practice.acronym.databinding.ActivityHomeBinding
import com.practice.acronym.presentation.screens.acronym.AcronymFragment

/*
 * Author: Rajkumar Srinivasan
 * Date: 06-Aug-2022
 */
class HomeActivity : AppCompatActivity() {
    lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AcronymFragment.newInstance()).commitNow()
        }
    }
}

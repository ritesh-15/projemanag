package com.example.projemanag.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.projemanag.databinding.ActivitySplashBinding
import com.example.projemanag.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {

    private var binding:ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // setting up the font
        val typeface :Typeface = Typeface.createFromAsset(assets,"Poppins-SemiBold.ttf")

        binding?.tvAppName?.typeface = typeface

        Handler().postDelayed({

            val currentUserID = FirestoreClass().getCurrentUserId()

            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding= null

    }
}
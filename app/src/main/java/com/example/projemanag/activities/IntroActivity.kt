package com.example.projemanag.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projemanag.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private var binding:ActivityIntroBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        // sign up button click
        binding?.btnSignUp?.setOnClickListener {
            handleSignUpClick()
        }


        // sign in button click
        binding?.btnSignIn?.setOnClickListener {
            handleSignInClick()
        }
    }

    // sign in button click handler
    private fun handleSignInClick(){
        startActivity(Intent(this, SignInActivity::class.java))
    }

    // sign up button click handler
    private fun handleSignUpClick(){
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
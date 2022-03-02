package com.example.projemanag.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.projemanag.databinding.ActivitySignInBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private var binding:ActivitySignInBinding? = null

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // setting up firebase auth
        auth = FirebaseAuth.getInstance()

        setUpActionBar()
    }

    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.signInToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Sign In"

        binding?.signInToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }

        // sign in button listener
        binding?.btnSignIn?.setOnClickListener {
            handleSignInClick()
        }
    }

    // sign in handler
    private fun handleSignInClick(){
        val email:String = binding?.tvEmail?.text.toString()
        val password:String = binding?.tvPassword?.text.toString()

        if(!validateForm(email,password)){
            return
        }

        showProgressDialog()

        signInUser(email,password)
    }


    // firebase sign in
    private fun signInUser(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                FirestoreClass().loadUserData(this)
            }else{

                Toast.makeText(this,task.exception!!.message,
                    Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }
    }


    // sign in success
    fun signInSuccess(user:User){
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    // validate the form
    private fun validateForm(email:String,password:String):Boolean{
        return when{
            email.isEmpty() -> {
                showErrorSnakeBar("Please enter email!")
                false
            }
            password.isEmpty() -> {
                showErrorSnakeBar("Please enter password!")
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
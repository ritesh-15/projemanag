package com.example.projemanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.projemanag.databinding.ActivitySignUpBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    private var binding:ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // set up action bar
        setUpActionBar()

        // sign up button listener
        binding?.btnSignUp?.setOnClickListener {
            handlerSignUpListener()
        }
    }


    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.signupToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Sign Up"

        binding?.signupToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }
    }

    // successful user registered
    fun userRegisteredSuccess(){
        Toast.makeText(
            this, "Registered success", Toast.LENGTH_SHORT
        ).show()

        hideProgressDialog()

        FirebaseAuth.getInstance().signOut()

        finish()
    }


    // sign up button listener
    private fun handlerSignUpListener(){
        val name:String = binding?.tvName?.text.toString().trim{it <= ' '}
        val email:String = binding?.tvEmail?.text.toString()
        val password:String = binding?.tvPassword?.text.toString()

        if(!validateForm(name,email,password)){
            return
        }

        showProgressDialog()

        registerUser(name,email,password)

    }

    // firebase register
    private fun registerUser(name:String,email:String,password:String){

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task ->
                if(!task.isSuccessful){

                    Toast.makeText(this,task.exception!!.message,
                        Toast.LENGTH_SHORT).show()

                }else {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val registeredEmail = firebaseUser.email!!

                    val user = User(firebaseUser.uid,name,registeredEmail)

                    // store the user in database
                    FirestoreClass().register(this,user)

                    finish()
                }
            }

    }

    // validate the form
    private fun validateForm(name:String,email:String,password:String):Boolean{
        return when{
            name.isEmpty() -> {
                showErrorSnakeBar("Please enter name!")
                false
            }
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
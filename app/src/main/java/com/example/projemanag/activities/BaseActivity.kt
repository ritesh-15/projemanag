package com.example.projemanag.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityBaseBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressOnce = false

    private var binding:ActivityBaseBinding? = null

    private lateinit var progressDialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    fun showProgressDialog(){
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.progress_bar_dialog)
        progressDialog.show()
    }

    fun hideProgressDialog(){
        progressDialog.dismiss()
    }

    // get current user id
    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressOnce){
            super.onBackPressed()
            return
        }

        doubleBackToExitPressOnce = true

        displayToastMessage("Please click once again to exit")

        Handler().postDelayed(
            {
                doubleBackToExitPressOnce = false
            }, 2000
        )
    }

    fun displayToastMessage(message:String){
        Toast.makeText(this,message,
            Toast.LENGTH_SHORT).show()
    }

    fun showErrorSnakeBar(message:String){

        val snakeBar = Snackbar.make(
            findViewById(androidx.appcompat.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )

        val snakeBarView = snakeBar.view

        snakeBarView.setBackgroundColor(
            ContextCompat.getColor(this,R.color.snakeBarColor)
        )

        snakeBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
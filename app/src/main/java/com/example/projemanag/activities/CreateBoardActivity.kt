package com.example.projemanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityCreateBoardBinding

class CreateBoardActivity : AppCompatActivity() {

    private var binding:ActivityCreateBoardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBoardBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        setUpActionBar()
    }

    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.createBoardToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Create Board"

        binding?.createBoardToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
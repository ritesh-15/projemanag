package com.example.projemanag.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityMembersBinding
import com.example.projemanag.models.Board
import com.example.projemanag.utils.Constants
import java.lang.Exception

class MembersActivity : AppCompatActivity() {

    private var binding:ActivityMembersBinding? = null

    private lateinit var mBoardDetails:Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        if(intent.hasExtra(Constants.BOARD_DETAILS)){
               mBoardDetails = intent.getSerializableExtra(Constants.BOARD_DETAILS) as Board
        }

        setUpActionBar()
    }

    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.membersToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Members"

        binding?.membersToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
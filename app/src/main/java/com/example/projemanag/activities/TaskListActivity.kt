package com.example.projemanag.activities

import android.os.Bundle
import com.example.projemanag.databinding.ActivityTaskListBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.utils.Constants

class TaskListActivity : BaseActivity() {

    private var binding:ActivityTaskListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // document id
        var documentId = ""

        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            documentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        showProgressDialog()
        FirestoreClass().getBoardDetails(this,documentId)
    }

    // set up action bar
    private fun setUpActionBar(title:String) {
        setSupportActionBar(binding?.taskActivityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = title

        binding?.taskActivityToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }
    }

    // get current board details
    fun boardDetails(board:Board){
        hideProgressDialog()
        setUpActionBar(board.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
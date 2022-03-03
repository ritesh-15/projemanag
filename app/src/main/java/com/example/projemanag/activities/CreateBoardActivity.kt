package com.example.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityCreateBoardBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var binding:ActivityCreateBoardBinding? = null

    private var mSelectedImageFileUri: Uri? = null

    private var mBoardImageUrl:String = ""

    private lateinit var mUserName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBoardBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        setUpActionBar()

        if(intent.getStringExtra(Constants.NAME)!!.isNotEmpty()){
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding?.boardImage?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                // get the permission
                Constants.showImageChooser(this)
            }else{
                // don't have read permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        // create board button listener
        binding?.btnCreateBoard?.setOnClickListener {
            handleCreateButtonListener()
        }

    }

    // handle create button listener
    private fun handleCreateButtonListener(){
        if(mSelectedImageFileUri != null)
            uploadImage()
        else{
            showProgressDialog()
            createBoard()
        }
    }


    // create board information
    private fun createBoard(){
        val assignedUsersList:ArrayList<String> = ArrayList()
        assignedUsersList.add(getCurrentUserId())

        val name:String = binding?.tvBoardName?.text.toString()

        val board = Board(
            name,
            mBoardImageUrl,
            mUserName,
            assignedUsersList
        )

        FirestoreClass().createNewBoard(this,board)
    }

    // after creating board successfully
    fun boardCreatedSuccessfully(){
        hideProgressDialog()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK &&
            requestCode == Constants.PICK_IMAGE_REQUEST_CODE &&
            data!!.data != null){
            mSelectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(binding?.boardImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }

        }
    }

    // after getting neccessary permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]
                == PackageManager.PERMISSION_GRANTED){
                // access the permission
                Constants.showImageChooser(this)
            }
        }else{
            // permission not granted
            Toast.makeText(this,
                "You have not granted the storage permission you can grant the storage" +
                        "permission from settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.createBoardToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Sign In"

        binding?.createBoardToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }

    }

    // upload image
    private fun uploadImage(){
        showProgressDialog()

        if(mSelectedImageFileUri != null){

            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference.child("BOARD_IMAGE_${System.currentTimeMillis()}" +
                        Constants.getExtensionOfFile(mSelectedImageFileUri!!)
                )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                    taskSnapshot ->
                val downloadUrl = taskSnapshot!!.metadata!!.reference!!.downloadUrl

                downloadUrl.addOnSuccessListener {
                        uri ->
                    mBoardImageUrl = uri.toString()
                    createBoard()
                }
            }.addOnFailureListener{
                    exeption -> Toast.makeText(this,exeption.message,Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
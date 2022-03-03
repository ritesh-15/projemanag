package com.example.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityProfileBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.jar.Manifest

class ProfileActivity : BaseActivity() {


    private var binding:ActivityProfileBinding? = null

    private var mSelectedImageFileUri:Uri? = null

    private var mProfileImageUri:String = ""

    private lateinit var mUserDetails:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpActionBar()

        FirestoreClass().loadUserData(this)

        binding?.userImage?.setOnClickListener {
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

        // update button listener
        binding?.btnUpdate?.setOnClickListener {
            handleUpdateClick()
        }
    }

    private fun handleUpdateClick(){
       if(mSelectedImageFileUri != null){
           uploadImage()
       }else{
           showProgressDialog()
           updateUserData()
       }
    }

    // update the user profile data
    private fun updateUserData(){
        val userHashMap = HashMap<String,Any>()

        if(mProfileImageUri.isNotEmpty() && mProfileImageUri != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageUri
        }

        if(binding?.tvName?.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = binding?.tvName?.text.toString()
        }

        if(binding?.tvMobile?.text.toString().toLong() != mUserDetails.mobile){
            userHashMap[Constants.MOBILE] = binding?.tvMobile?.text.toString().toLong()
        }

        FirestoreClass().updateUserProfileData(this,userHashMap)

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
                    .into(binding?.userImage!!)
            }catch (e:IOException){
                e.printStackTrace()
            }

        }
    }



    // upload image
    private fun uploadImage(){
        showProgressDialog()

        if(mSelectedImageFileUri != null){

            val sRef:StorageReference = FirebaseStorage.getInstance("gs://projemang-f3bed.appspot.com/")
                .reference.child("USER_IMAGE_${System.currentTimeMillis()}" +
                        Constants.getExtensionOfFile(mSelectedImageFileUri!!)
                )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot ->
                val downloadUrl = taskSnapshot!!.metadata!!.reference!!.downloadUrl

                downloadUrl.addOnSuccessListener {
                    uri ->
                    mProfileImageUri = uri.toString()
                    updateUserData()
                }
            }.addOnFailureListener{
                exeption -> Toast.makeText(this,exeption.message,Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }

        }

    }

    // updating profile success
    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
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



    // set user data in ui
    fun setUserDataInUI(user:User){

        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(binding?.userImage!!)

        binding?.tvName?.setText(user.name)

        binding?.tvEmail?.setText(user.email)

        if(user.mobile != 0L){
            binding?.tvMobile?.setText(user.mobile.toString())
        }
    }

    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.profileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "My Profile"

        binding?.profileToolbar?.setNavigationOnClickListener{
            // setting up back button
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
package com.example.projemanag.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.example.projemanag.activities.ProfileActivity

object Constants {
    // users
    const val USERS :String = "Users"
    const val NAME:String = "name"
    const val EMAIL:String = "email"
    const val IMAGE:String = "image"
    const val MOBILE:String = "mobile"

    // board
    const val BOARDS:String = "Boards"

     const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2

     fun showImageChooser(activity:Activity){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    // check the file type
  fun getExtensionOfFile(uri: Uri):String{
        return uri.toString().
        substring(uri.toString().lastIndexOf("."))
    }
}
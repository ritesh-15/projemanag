package com.example.projemanag.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projemanag.activities.MainActivity
import com.example.projemanag.activities.ProfileActivity
import com.example.projemanag.activities.SignInActivity
import com.example.projemanag.activities.SignUpActivity
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.lang.Exception

class FirestoreClass {

    private val firebaseStore:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth:FirebaseAuth = FirebaseAuth.getInstance()

    fun register(activity:SignUpActivity,user:User){
        firebaseStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun loadUserData(activity:Activity){
            firebaseStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document ->
                    val name:String = document?.data?.get("name").toString()
                    val email:String = document?.data?.get("email").toString()
                    val mobile:Long = document?.data?.get("mobile").toString().toLong()
                    val id:String = document?.data?.get("id").toString()
                    val image:String = document?.data?.get("image").toString()
                    val fcmToken:String = document?.data?.get("fcmToken").toString()

                    val currentLoggedInUser = User(id, name, email, image, mobile, fcmToken)

                    when(activity){
                        is SignInActivity -> {
                            activity.signInSuccess(currentLoggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(currentLoggedInUser)
                        }
                        is ProfileActivity -> {
                            activity.setUserDataInUI(currentLoggedInUser)
                        }
                    }

                }.addOnFailureListener{
                    when(activity){
                        is SignInActivity -> {
                            activity.hideProgressDialog()
                        }
                        is MainActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                }
    }


    fun  updateUserProfileData(activity: ProfileActivity,
                               userHashMap:HashMap<String,Any>){
        firebaseStore.collection(Constants.USERS).
        document(getCurrentUserId()).
        update(userHashMap).
        addOnSuccessListener {
            Toast.makeText(activity,"Profile updated successfully",
                Toast.LENGTH_SHORT).show()
            activity.profileUpdateSuccess()
        }.addOnFailureListener{
            exepetion -> activity.hideProgressDialog()
            Toast.makeText(activity,"Something went wrong!",
                Toast.LENGTH_SHORT).show()

        }
    }

    fun  getCurrentUserId():String{

        val currentUser = auth.currentUser

        var currentUserID = ""

        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

}
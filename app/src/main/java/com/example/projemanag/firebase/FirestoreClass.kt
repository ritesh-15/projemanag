package com.example.projemanag.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projemanag.activities.*
import com.example.projemanag.models.Board
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
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

    fun loadUserData(activity:Activity, readBoardsList:Boolean = false){
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
                            activity.updateNavigationUserDetails(currentLoggedInUser,readBoardsList)
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

    fun createNewBoard(activity: CreateBoardActivity,board:Board){
        firebaseStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity,"Board created successfully!",Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener {
                exepetion ->
                activity.hideProgressDialog()
                Log.e("BOARD_CREATED_ERROR",exepetion.message!!)
            }
    }


    fun getBoardsList(activity:MainActivity){
        firebaseStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO,getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->

                Log.i("DOCUMENT_BOARD",document.toString())

                val boardsList:ArrayList<Board> = ArrayList()

                for(i in document.documents){

                    val name:String = i.data?.get(Constants.NAME).toString()
                    val image:String = i.data?.get(Constants.IMAGE).toString()
                    val createdBy:String = i.data?.get(Constants.CREATED_BY).toString()
                    val assignedTo:ArrayList<String> = ArrayList()

                    val assignedToArray =i.data?.get(Constants.ASSIGNED_TO).toString()

                   for(j in assignedToArray){
                       assignedTo.add(j.toString())
                   }

                    val board = Board(name,image,createdBy,assignedTo,i.id)

                    boardsList.add(board)
                }

                activity.populateBoardList(boardsList)
            }.addOnFailureListener {
                exeption ->
                Log.e("DOCUMENT_ERROR",exeption.message!!)
            }
    }

    fun getBoardDetails(activity:TaskListActivity,documentId:String){

        firebaseStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->

                Log.i("DOCUMENT_BOARD",document.toString())

                activity.boardDetails(document.toObject(Board::class.java)!!)

            }.addOnFailureListener {
                    exeption ->
                Log.e("DOCUMENT_ERROR",exeption.message!!)
            }


    }

}
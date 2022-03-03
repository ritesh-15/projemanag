package com.example.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.adapters.BoardItemsAdapter
import com.example.projemanag.databinding.ActivityMainBinding
import com.example.projemanag.databinding.NavHeaderMainBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {

    companion object{
        const val MY_PROFILE_REQUEST_CODE = 11
        const val CREATE_BOARD_REQUEST_CODE = 12
    }

    private var binding:ActivityMainBinding? = null

    private var navBarBinding :NavHeaderMainBinding? =  null

    private lateinit var mUserName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        navBarBinding = NavHeaderMainBinding.inflate(layoutInflater)

        setUpActionBar()

        binding?.navView?.setNavigationItemSelectedListener(this)

        // load user data
        FirestoreClass().loadUserData(this,true)


        // create board button lister
        binding?.appBarInclude?.createBoard?.setOnClickListener {

           val intent = Intent(this,CreateBoardActivity::class.java)

            intent.putExtra(Constants.NAME , mUserName)

           startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)

        }

    }

    // populate board
    fun populateBoardList(boards:ArrayList<Board>){

        hideProgressDialog()

        if(boards.size > 0){
            binding?.appBarInclude?.mainActivityContent?.rvBoardList?.visibility = View.VISIBLE
            binding?.appBarInclude?.mainActivityContent?.noBoardsText?.visibility = View.GONE

            binding?.appBarInclude?.mainActivityContent?.rvBoardList?.layoutManager = LinearLayoutManager(this)

            binding?.appBarInclude?.mainActivityContent?.rvBoardList?.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this,boards)

            adapter.setOnClickListener(object:BoardItemsAdapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity,TaskListActivity::class.java)

                    intent.putExtra(Constants.DOCUMENT_ID,model.documentId)

                    startActivity(intent)
                }
            })

            binding?.appBarInclude?.mainActivityContent?.rvBoardList?.adapter = adapter
        }else{
            binding?.appBarInclude?.mainActivityContent?.rvBoardList?.visibility = View.GONE
            binding?.appBarInclude?.mainActivityContent?.noBoardsText?.visibility = View.VISIBLE

        }

    }

    // set up action bar
    private fun setUpActionBar() {
        setSupportActionBar(binding?.appBarInclude?.toolbarMainActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.appBarInclude?.toolbarMainActivity?.setNavigationIcon(R.drawable.ic_action_nav_menu)

        binding?.appBarInclude?.toolbarMainActivity?.setNavigationOnClickListener {
            // toggle the drawer
            toggleDrawer()
        }

    }

    // update navigation user details
    fun updateNavigationUserDetails(user:User,readBoardsList:Boolean){

        mUserName = user.name



        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById(R.id.nav_user_image))

        val tvUserName:TextView = findViewById(R.id.nav_user_name)

        tvUserName.text = user.name

        if(readBoardsList){
            showProgressDialog()
            FirestoreClass().getBoardsList(this)
        }
    }

    // toggle the drawer
    private fun toggleDrawer(){
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){

            binding?.drawerLayout?.closeDrawer(GravityCompat.START)

        }else{
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }else{
           doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MY_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            FirestoreClass().loadUserData(this)
        }else if(requestCode == CREATE_BOARD_REQUEST_CODE && resultCode == Activity.RESULT_OK ){
            FirestoreClass().getBoardsList(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

         when(item.itemId){

             R.id.nav_my_profile -> {
                 startActivityForResult(Intent(this,ProfileActivity::class.java),
                     MY_PROFILE_REQUEST_CODE)
                 if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
                     binding?.drawerLayout?.closeDrawer(GravityCompat.START)
                 }
             }

             R.id.nav_sign_out -> {
                 FirebaseAuth.getInstance().signOut()
                 val intent = Intent(this,IntroActivity::class.java)
                 // intent will be first in all intents
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                 startActivity(intent)
                 finish()
             }

             else -> {
                 binding?.drawerLayout?.closeDrawer(GravityCompat.START)
             }

         }

        return true

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        navBarBinding = null
    }

}
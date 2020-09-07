package com.example.pathshala

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_regester.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var firebaseAuth: FirebaseAuth? = null
    private var fsdb: FirebaseFirestore? = null
    private var currentusername = ""
    private var currentuseremail = ""
    private var currentusermobile = ""
    private var Fetchedcurrentuser:MutableMap<String, Any> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        firebaseAuth = FirebaseAuth.getInstance()
        fsdb = FirebaseFirestore.getInstance()


        val user = firebaseAuth?.getCurrentUser()
        var currentuser  = user?.getEmail().toString() //fetched the email id of current user from firebase auth

        fetchcurrentuser(currentuser) // fetched current user data from firestore


        //navuseremail.setText()


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displayScreen(-1)
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }



    fun displayScreen(id: Int){

        // val fragment =  when (id){

        when (id){
            R.id.nav_home -> {
                Toast.makeText(this,"home",Toast.LENGTH_LONG).show()

            }

            R.id.nav_gallery -> {
                Toast.makeText(this,"lolo coco",Toast.LENGTH_LONG).show()
            }

            R.id.nav_slideshow -> {
                Toast.makeText(this,"lolo coco",Toast.LENGTH_LONG).show()
                //supportFragmentManager.beginTransaction().replace(R.id.relativelayout, MoviesFragment()).commit()

            }

            R.id.nav_logout -> {
                firebaseAuth?.signOut()
                Toast.makeText(this,"Logout SuccessFully",Toast.LENGTH_LONG).show()
                startActivity(Intent(this, Login_Activity::class.java))

            }


        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        displayScreen(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }




    private fun fetchcurrentuser(currentuser:String)
    {

            val docRef = fsdb?.collection("REGISTERED-USERS")?.document(currentuser)
        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d("DATAfound", "DocumentSnapshot data: ${document.data}")
                    Fetchedcurrentuser= document.data!!
                    Log.d("fetched", "DocumentSnapshot data: ${Fetchedcurrentuser}")

                    currentusername = Fetchedcurrentuser.get("NAME").toString()
                    currentuseremail= Fetchedcurrentuser.get("EMAIL").toString()
                    currentusermobile = Fetchedcurrentuser.get("MOBILENO").toString()


                    showgwtdata()

                } else {
                    Log.d("Not EXIST", "No such document")

                }
            }
            ?.addOnFailureListener { exception ->
                Log.d("ERROR OCCURRED","get failed with ", exception)
            }







    }

    private fun showgwtdata() {

        Toast.makeText(this,currentuseremail+currentusermobile+currentusername,Toast.LENGTH_LONG).show()
        navuseremail.setText(currentuseremail)
        navusername.setText(currentusername)

    }

    private fun sharedpref() {



    }


}
package com.example.up2dateJEC

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.up2dateJEC.Account_Setting
import com.up2dateJEC.Fragment.HomeFragment
import com.up2dateJEC.Notification.FirebaseService
import com.up2dateJEC.ScrapJec
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

const val TOPIC = "testtopic"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    private var firebaseAuth: FirebaseAuth? = null
    private var fsdb: FirebaseFirestore? = null
    var backpress = 0


    var currentusername = ""
    var currentuseremail = ""
    var currentusermobile = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        FirebaseService.sharedPref = this.getSharedPreferences("sharedPreftoken", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token

        }

        firebaseAuth = FirebaseAuth.getInstance()
        fsdb = FirebaseFirestore.getInstance()




        val user = firebaseAuth?.getCurrentUser()
        var currentuser  = user?.getEmail().toString() //fetched the email id of current user from firebase auth



        //getting value from shared prefrance
        var(CN,CE,CM)  = getcurrentuserdata()
        currentusername=CN.toString()
        currentuseremail=CE.toString()
        currentusermobile=CM.toString()

        val hView = nav_view.getHeaderView(0)
        val ViewName = hView.findViewById(R.id.navusernameTV) as TextView
        val ViewEmail = hView.findViewById(R.id.navuseremailTV) as TextView



        if (CN.toString().equals(null)||CN.toString().equals(""))
        {  fetchcurrentuser(currentuser) /* fetched current user data from firestore*/ }

        if (CN.toString().isNotEmpty())
        {

            ViewName.setText(currentusername)
            ViewEmail.setText(currentuseremail)
            //navuseremail.text = currentuseremail.toString()
            Toast.makeText(this,currentusername+currentuseremail+currentusermobile,Toast.LENGTH_SHORT).show()
        }







        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            startActivity(Intent(this,ScrapJec::class.java))
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displayScreen(-1)



        addFragment(HomeFragment())



    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            backpress +=1
            if (backpress==1)
                Toast.makeText(this,"Press Again To Exit",Toast.LENGTH_SHORT).show()
            else if (backpress==2)
                finish()


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

            R.id.nav_account -> {

                startActivity(Intent(this,Account_Setting::class.java))
            }

            R.id.nav_logout -> {

                val sharepref: SharedPreferences = getSharedPreferences("currentuserdetails",0)
                val editor: SharedPreferences.Editor = sharepref.edit()
                editor.clear()
                editor.apply()
                finish()
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


    private fun fetchcurrentuser(currentuser:String){

        var Fetchedcurrentuser:MutableMap<String, Any> = HashMap()

        val docRef = fsdb?.collection("REGISTERED-USERS")?.document(currentuser)
        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d("DATAfound", "DocumentSnapshot data: ${document.data}")
                    Fetchedcurrentuser.putAll(document.data!!)

                    Log.d("fetched", "DocumentSnapshot data: ${Fetchedcurrentuser}")


                    var username  = Fetchedcurrentuser.get("NAME").toString()
                    var useremail= Fetchedcurrentuser.get("EMAIL").toString()
                    var usermobile = Fetchedcurrentuser.get("MOBILENO").toString()


                    //Toast.makeText(this,username+useremail+usermobile,Toast.LENGTH_LONG).show()


                    /* sharepref.edit().putString("currentuseremail",currentuseremail).apply()
                     sharepref.edit().putString("currentusermobile",currentusermobile).apply()
                     sharepref.edit().putString("currentusername",currentusername).apply()*/

                    navusernameTV.setText(username)
                    navuseremailTV.setText(useremail)

                    sharedprefuserdata(username,useremail,usermobile)


                } else {
                    Log.d("Not EXIST", "No such document")


                }

            }
            ?.addOnFailureListener { exception ->
                Log.d("ERROR OCCURRED","get failed with ", exception)
            }



    }


    private fun sharedprefuserdata(username:String,useremail:String,usermobile:String) {


        val sharepref: SharedPreferences = getSharedPreferences("currentuserdetails",0)


        sharepref.edit().putString("currentusername",username).apply()
        sharepref.edit().putString("currentuseremail",useremail).apply()
        sharepref.edit().putString("currentusermobile",usermobile).apply()

        Toast.makeText(this,"data saved in shared pref",Toast.LENGTH_LONG).show()

    }


    private fun getcurrentuserdata(): Array<String?> {


        val sharepref: SharedPreferences = getSharedPreferences("currentuserdetails",0)

        var getcurrentusername = sharepref.getString("currentusername","")
        var getcurrentuseremail = sharepref.getString("currentuseremail","")
        var getcurrentusermobile = sharepref.getString("currentusermobile","")


        return arrayOf(getcurrentusername,getcurrentuseremail,getcurrentusermobile)
    }


    private fun addFragment(fragment: Fragment){

        val fragmentManager: androidx.fragment.app.FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fragment_holder, fragment).commit()

    }



}
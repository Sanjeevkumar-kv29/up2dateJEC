package com.example.up2dateJEC

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.up2dateJEC.Notification.FirebaseService
import kotlinx.android.synthetic.main.activity_accountsetting.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regester.*


class RegesterActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val fsdb = FirebaseFirestore.getInstance()

    var email =""
    var pass =""
    var phoneno =""
    var name =""
    var isemailexist =""

    private var pD: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regester)

        pD = ProgressDialog(this)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        FirebaseService.sharedPref = this.getSharedPreferences("sharedPreftoken", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }

        regbtn.setOnClickListener{

            val regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$".toRegex()

            if (regemail.text.toString().isEmpty()){
                regemail.setError("Enter Email ID")
            }
            else if (regpass.text.toString().isEmpty()){
                regpass.setError("Enter The Password")
            }
            else if (regpass.text.toString().length<6){
                regpass.setError("Password length should be greater the 6")
            }
            else if (regmobile.text.toString().isEmpty()){
                regmobile.setError("Enter The Active Mobile No.")
            }
            else if (regmobile.text.toString().length<10){
                regmobile.setError("Enter a valid Mobile no.")
            }
            else if (regname.text.toString().isEmpty()){
                regname.setError("Enter Name")
            }
            else if (regemail.text.toString().matches(regex)){


                pD?.setMessage("Please Wait ")
                pD?.show()

            email = regemail.text.toString()
            pass = regpass.text.toString()
            phoneno = regmobile.text.toString()
            name=regname.text.toString()

            fbfsprecheck(email)
        }
            else{
            regemail.setError("enter valid email")
        }




        }



        reg2log.setOnClickListener {

            finish()
            startActivity(Intent(this,Login_Activity::class.java))
            overridePendingTransition(R.anim.leftin,R.anim.rightout)
        }

    }

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this,Login_Activity::class.java))
        overridePendingTransition(R.anim.leftin,R.anim.rightout)

    }



    private fun fbfsprecheck(emailcheck:String){




        val docRef = fsdb.collection("REGISTERED-USERS").document(emailcheck)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d("email EXIST", "DocumentSnapshot data: ${document.data}")
                    pD?.dismiss()
                    regemail.setError("email already registered")
                    Toast.makeText(this,"ALREADY REGISTERED PLEASE LOGIN ",Toast.LENGTH_LONG).show()


                } else {

                    Log.d("User Not EXIST", "user not registered in firestore")


                   // Toast.makeText(this,"bsdk registration kr",Toast.LENGTH_LONG).show()

                    /*val intent = Intent(this, Otpverification::class.java)    /*MOBILE NUMBER AUTHENTICATION OTP AUTHENTICATION*/
                   intent.putExtra("Email", email)
                   intent.putExtra("Pass", pass)
                   intent.putExtra("Phone", phoneno)
                   intent.putExtra("name",name)
                   startActivity(intent)   */

                    fbfs(name,email,phoneno)

                    val intent = Intent(this,EmailAuth::class.java)
                    intent.putExtra("email",email)
                    intent.putExtra("pass",pass)
                    intent.putExtra("mobile",phoneno)
                    intent.putExtra("name",name)
                    startActivity(intent)
                    pD?.dismiss()

                    finish()


                }
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR OCCURRED","get failed with ", exception)
            }




    }


    private fun fbfs(name: String,email: String,mobileno: String)
    {
        val user: MutableMap<String, Any> = HashMap()
        user["NAME"] = name
        user["EMAIL"] = email
        user["MOBILENO"] = mobileno
        user["DATE-OF-BIRTH"] = ""
        user["COLLEGE-NAME"] = ""
        user["USER-ADDRESS"] = ""
        user["LINKEDIN-PROFILE"] = ""
        user["FCM-TOKEN"] = FirebaseService.token.toString()

        fsdb.collection("REGISTERED-USERS").document(email)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Success",
                    "DocumentSnapshot added with ID: " + documentReference
                )
            }
            .addOnFailureListener { e -> Log.w("error", "Error adding document", e) }

    }




}

package com.example.up2dateJEC

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_email_auth.*


class EmailAuth : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    var fsdb = FirebaseFirestore.getInstance()
    var email =""
    var pass=""
    var mobile=""
    var name=""
    var user_email=""
    var user_password=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_auth)


        button2log.visibility = View.GONE
        firebaseAuth = FirebaseAuth.getInstance()


        val intent=intent
        email=intent.getStringExtra("email").trim()
        pass = intent.getStringExtra("pass")
        mobile = intent.getStringExtra("mobile")
        name= intent.getStringExtra("name")

        user_email = email.toString().trim { it <= ' ' }
        user_password = pass.toString().trim { it <= ' ' }

        emailauth()


        button2log.setOnClickListener {
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

    fun emailauth()
{
    firebaseAuth?.createUserWithEmailAndPassword(user_email, user_password)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendEmailVerification()
            } else {
                Toast.makeText(
                    this,
                    "Registration Failed!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}





    private fun sendEmailVerification() {

        val firebaseUser = firebaseAuth?.getCurrentUser()
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {


                //fbfs(name,email,mobile)
                Toast.makeText(this,"Verification mail sent! Successfully",Toast.LENGTH_LONG).show()
                firebaseAuth?.signOut()
                progressbaremail.visibility = View.GONE
                button2log.visibility = View.VISIBLE


            } else {
                Toast.makeText(
                    this,
                    "Verification mail has'nt been sent!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }





}
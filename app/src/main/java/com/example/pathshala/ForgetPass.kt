package com.example.pathshala

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgetPass : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)


        mAuth = FirebaseAuth.getInstance()


        resetbttn.setOnClickListener(){

            resetbttn.visibility = View.GONE

            var forgotemail = forgotemailid.text.toString()
            forgotemail.toString().trim { it <= ' ' }

            mAuth!!.sendPasswordResetEmail(forgotemail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                        ftv.visibility = View.VISIBLE
                        forg2log.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        forg2log.setOnClickListener {

            startActivity(Intent(this,Login_Activity::class.java))
            overridePendingTransition(R.anim.leftin,R.anim.rightout)
        }

    }

    override fun onBackPressed() {

    }

}
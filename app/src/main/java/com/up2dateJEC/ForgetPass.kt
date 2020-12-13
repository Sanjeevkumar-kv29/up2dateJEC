package com.example.up2dateJEC

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_pass.*
import kotlinx.android.synthetic.main.activity_regester.*

class ForgetPass : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)


        mAuth = FirebaseAuth.getInstance()


        resetbttn.setOnClickListener(){

            if (forgotemailid.text.toString().isEmpty()){
                forgotemailid.setError("Enter Email ID")
            }

            else{

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


        }

        forg2log.setOnClickListener {

            startActivity(Intent(this,Login_Activity::class.java))
            overridePendingTransition(R.anim.leftin,R.anim.rightout)
        }

    }

    override fun onBackPressed() {


        startActivity(Intent(this,Login_Activity::class.java))
        overridePendingTransition(R.anim.leftin,R.anim.rightout)

    }

}
package com.example.up2dateJEC

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_otpverification.*
import java.util.concurrent.TimeUnit


class Otpverification : AppCompatActivity() {


    var email = ""
    var pass = ""
    var phone = ""

    val mAuth: FirebaseAuth= FirebaseAuth.getInstance()

    private var mVerificationId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.up2dateJEC.R.layout.activity_otpverification)


        val intent = intent
        email = intent.getStringExtra("Email")
        pass = intent.getStringExtra("Pass")
        phone = intent.getStringExtra("Phone")

        sendVerificationCode(phone);
        Toast.makeText(this,"OTP SEND SUCCESSFULLY",Toast.LENGTH_SHORT).show()
    }


    private fun sendVerificationCode(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
    }

    //the callback to detect the verification status
    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {

                    //automatic verify is sim card exist in same phone
                    enterotp.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@Otpverification, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        //verification successful we will start the profile activity
                        FireStoreReg()
                        Toast.makeText(this,"OTP verified",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login_Activity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {

                        //verification unsuccessful.. display an error message
                        var message =
                            "Somthing is wrong, we will fix it soon..."
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                        }

                        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                    }
                })
    }


    fun FireStoreReg(){

        val FSDB :FirebaseFirestore = FirebaseFirestore.getInstance()

        val datamap:MutableMap<String,Any> = HashMap<String,Any>()

        datamap.put("email",email)
        datamap.put("pass",pass)
        datamap.put("phone",phone)

        FSDB.collection("REGISTERDUSER").document(phone).set(datamap)
            .addOnSuccessListener {
                Toast.makeText(this,"Registration Successful Please Login",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Connection ERROR",Toast.LENGTH_LONG).show()
            }


    }

}
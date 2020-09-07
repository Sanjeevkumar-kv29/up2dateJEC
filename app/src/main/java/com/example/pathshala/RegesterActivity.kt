package com.example.pathshala

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_regester.*


class RegesterActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val fsdb = FirebaseFirestore.getInstance()

    var email =""
    var pass =""
    var phoneno =""
    var name =""
    var isemailexist =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regester)




        regbtn.setOnClickListener{

                 email = regemail.text.toString()
                 pass = regpass.text.toString()
                 phoneno = regmobile.text.toString()
                 name=regname.text.toString()


                 fbfsprecheck(email)


        }



        reg2log.setOnClickListener {

            startActivity(Intent(this,Login_Activity::class.java))
            overridePendingTransition(R.anim.leftin,R.anim.rightout)
        }

    }



    private fun fbfsprecheck(emailcheck:String){
        val docRef = fsdb.collection("REGISTERED-USERS").document(emailcheck)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d("email EXIST", "DocumentSnapshot data: ${document.data}")

                    regemail.setError("email already registered")
                    Toast.makeText(this,"ALREADY REGISTERED PLEASE LOGIN ",Toast.LENGTH_LONG).show()


                } else {
                    Log.d("Not EXIST", "No such document")


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

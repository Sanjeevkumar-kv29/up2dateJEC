package com.up2dateJEC

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.up2dateJEC.R
import com.google.firebase.firestore.FirebaseFirestore
import com.up2dateJEC.Fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_student_notify.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StudentNotify : AppCompatActivity() {
    private var pD: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_notify)
        pD = ProgressDialog(this)

        btnSend.setOnClickListener {
            Log.e("pushNoti", "pushNoti")
            val title = etTitle.text.toString()
            val message = etMessage.text.toString()
            pD?.setMessage("Please Wait Submitting Notice...., ")
            pD?.show()
            sendnotitodb(title,message)
        }

        btnback.setOnClickListener {
           finish()
        }

    }

    private fun sendnotitodb(title: String, message: String) {

        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["NotificationTOPIC"] = title
        data["NotificationDISC"] = message
        data["DEPARTMENTorTOPIC"] = "ByStudent"
        data["publishstats"] = "false"
        val df: DateFormat = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss")
        val date: String = df.format(Calendar.getInstance().time)


        db.collection("NOTIFICATIONs").document(date)
            .set(data)
            .addOnSuccessListener { documentReference ->
                pD?.dismiss()
                Toast.makeText(this, "Notification Sent for Validation", Toast.LENGTH_LONG).show()

                finish()
            }
            .addOnFailureListener { e ->
                pD?.dismiss()
                Toast.makeText(this, "Error Sending Notification", Toast.LENGTH_LONG).show()
            }
    }



}
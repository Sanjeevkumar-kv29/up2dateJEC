package com.up2dateJEC

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.up2dateJEC.MainActivity
import com.example.up2dateJEC.R
import com.google.firebase.firestore.FirebaseFirestore
import com.up2dateJEC.Adapter.NotificationRVAdapter
import com.up2dateJEC.Adapter.SubCategoryItemRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_category__detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.ArrayList

class Category_Detail : AppCompatActivity() {


    private val NoticeTitle = ArrayList<String>()
    private val NoticeBody = ArrayList<String>()
    private val Department = ArrayList<String>()
    private val NoticeTime  = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category__detail)

        val intent = intent
        val ClickedCategoryName = intent.getStringExtra("CategoryName")

        DetailCategoryName.setText(ClickedCategoryName)

        getSubCategoryDetails(ClickedCategoryName)



        BackArrow.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun getSubCategoryDetails(Category:String) {

        NoticeTime.clear()
        NoticeBody.clear()
        NoticeTitle.clear()
        Department.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("NOTIFICATIONs").addSnapshotListener {
                snapshot, e ->
            // if there is an exception we want to skip.
            if (e != null) {
                Log.w("Failed", "Listen Failed", e)
                return@addSnapshotListener
            }
            // if we are here, we did not encounter an exception
            if (snapshot != null) {
                // now, we have a populated shapshot
                val documents = snapshot.documents

                documents.forEach {

                    if (it.get("DEPARTMENTorTOPIC").toString()==Category){

                        NoticeTime.add(it.id)
                        NoticeBody.add(it.get("NotificationDISC").toString())
                        NoticeTitle.add(it.get("NotificationTOPIC").toString())
                        Department.add(it.get("DEPARTMENTorTOPIC").toString())

                    }

                }

                NoticeTitle.reverse()
                NoticeBody.reverse()
                NoticeTime.reverse()
                Department.reverse()
                NotiRecyclerView()
            }
        }


    }





    private fun NotiRecyclerView() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView: RecyclerView = SubcatRECYCLERlyt
        recyclerView.layoutManager = layoutManager
        val adapter = NotificationRVAdapter(
            this,
            NoticeTitle,
            NoticeBody,
            Department,
            NoticeTime
        )
        recyclerView.adapter = adapter
    }

}
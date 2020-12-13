package com.up2dateJEC.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.up2dateJEC.R
import com.google.firebase.firestore.FirebaseFirestore
import com.up2dateJEC.Adapter.CategoryItemRecyclerViewAdapter
import com.up2dateJEC.Adapter.NotificationRVAdapter
import com.up2dateJEC.StudentNotify
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private val CategoryNameList = ArrayList<String>()
    private val CategoryIconUrlList = ArrayList<String>()


    private val NoticeTitle = ArrayList<String>()
    private val NoticeBody = ArrayList<String>()
    private val Department = ArrayList<String>()
    private val NoticeTime  = ArrayList<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getCategoryIconImages()
        AllNotification()

        Addnotification.setOnClickListener {

            startActivity(Intent(this.activity, StudentNotify::class.java))
        }

    }


//---------------------------------------------------------------------------------------------------------------------------------------------------------//
    private fun getCategoryIconImages() {


        Log.d(TAG, "initImageBitmaps: preparing bitmaps.")
        CategoryIconUrlList.add("https://www.iconsdb.com/icons/preview/persian-red/house-xxl.png")
        CategoryNameList.add("Home")

        val db = FirebaseFirestore.getInstance()
        db.collection("DEPARTMENT-TOPIC").addSnapshotListener {
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
                    CategoryIconUrlList.add(it.get("CategoryIconUrl") as String)
                    CategoryNameList.add(it.id)
                    Log.w("catIconURL",it.get("CategoryIconUrl") as String, e)
                    Log.w("catIconName",it.id, e)
                }

            }
        }

        CatICONRecyclerView()
    }




    private fun CatICONRecyclerView() {

        Log.d(TAG, "initRecyclerView: init recyclerview")
        val layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView: RecyclerView = CatrecyclerView
        recyclerView.layoutManager = layoutManager
        val adapter = CategoryItemRecyclerViewAdapter(
            this.activity,
            CategoryNameList,
            CategoryIconUrlList
        )
        recyclerView.adapter = adapter

    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------//


    fun AllNotification() {

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
                    // BannerImageUrlList.add(SlideModel(it.get("BannerImageURL").toString()))

                    Log.w("stats",it.get("publishstats").toString(), e)
                    if (it.get("publishstats")=="true"){

                        Log.w("statsdata",it.get("NotificationTOPIC").toString(), e)
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
                //Log.w("BannImgURL",banurl, e)

            }
        }
    }



    private fun NotiRecyclerView() {


        Log.d(TAG, "initRecyclerView: init recyclerview")
        val layoutManager =
            LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        val recyclerView: RecyclerView = Homenotification
        recyclerView.layoutManager = layoutManager
        val adapter = NotificationRVAdapter(
            this.activity,
            NoticeTitle,
            NoticeBody,
            Department,
            NoticeTime
        )
        recyclerView.adapter = adapter

    }



}

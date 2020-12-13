package com.up2dateJEC

import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.up2dateJEC.R
import com.up2dateJEC.Adapter.NotificationRVAdapter
import kotlinx.android.synthetic.main.activity_scrap_jec.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class ScrapJec : AppCompatActivity() {




    var mNotice = ArrayList<String>()
    var mFdata = ArrayList<String>()

    var mTitle = ArrayList<String>()
    var mdepat = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrap_jec)
        getHtmlFromWeb()


    }



    private fun getHtmlFromWeb() {



        var Notice = ArrayList<String>()
        var Fdata = ArrayList<String>()

        var Title = ArrayList<String>()
        var depat = ArrayList<String>()

        Thread(Runnable {


            try {
                val doc: Document = Jsoup.connect("https://www.jecjabalpur.ac.in/advertisement_forms/frm_utility_moredtls.aspx?CateId=GK4PkdD4BPQ%3D").get()

                // val title: String = doc.title()
                //val links: Elements = doc.select("a")

                val fdata: Elements = doc.getElementById("ctl00_ContentPlaceHolder1_WC_MoreUtility1_GVView").getElementsByTag("span")
                val notice: Elements = doc.getElementById("ctl00_ContentPlaceHolder1_WC_MoreUtility1_GVView").getElementsByTag("a")

                Log.d("fetchdate",fdata.toString())
                Log.d("fetchdetail",notice.toString())

                for (name in notice) {

                    //stringBuilder.append("\n").append("Link :").append(link.attr("href")).append("\n").append("Text :").append(link.text())
                    Notice.add(name.getElementsByTag("a").text().toString())
                    Log.d("NoticeFetch",Notice.toString())
                    Title.add("Official Notice")


                }

                for (name in fdata) {

                    if (name.getElementsByTag("span").text().toString().contains(Regex("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}\$"))==true)
                    {

                        Fdata.add(name.getElementsByTag("span").text().toString())
                        Log.d("DateFetch",Fdata.toString())

                    }
                    depat.add("Jec Jabalpur")

                }


                Log.d("NoticeFetch",Notice.size.toString())
                Log.d("NoticeFetch",Fdata.size.toString())
                Log.d("NoticeFetch",Title.size.toString())
                Log.d("NoticeFetch",depat.size.toString())

                val arraylength = arrayOf(Notice.size,Fdata.size,Title.size,depat.size)
                val min = arraylength.min()!! -1

                for (i in 0..min!!){

                    mNotice.add(Notice[i])
                    mFdata.add(Fdata[i])
                    mTitle.add(Title[i])
                    mdepat.add(depat[i])

                }

            } catch (e: IOException) {
                Notice.add("Error : "+(e.message)+("\n"))
            }


        runOnUiThread {

                    ScrapRecyclerView()


                    }
        }).start()


    }


    private fun ScrapRecyclerView() {


        Log.d("RV", "initRecyclerView: init recyclerview")
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView: RecyclerView = recyclerViewScrap
        recyclerView.layoutManager = layoutManager
        val adapter = NotificationRVAdapter(
            this,
            mTitle,
            mNotice,
            mdepat,
            mFdata
        )
        recyclerView.adapter = adapter
    }



}
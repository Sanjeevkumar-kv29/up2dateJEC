package com.up2dateJEC

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.up2dateJEC.MainActivity
import com.example.up2dateJEC.R
import kotlinx.android.synthetic.main.activity_category__detail.BackArrow
import kotlinx.android.synthetic.main.sub_category_details.*

class SubCategoryDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_category_details)

        val intent = intent
        val ClickedCategoryName = intent.getStringExtra("CategoryName")

        DetailSubCategoryName.setText(ClickedCategoryName)




        BackArrow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }



    }



}
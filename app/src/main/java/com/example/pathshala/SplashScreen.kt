package com.example.pathshala

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



            Handler().postDelayed(
                {

                    startActivity(Intent(this,Login_Activity::class.java))
                    overridePendingTransition(R.anim.leftout,R.anim.rightin)

                },3000 )
        }


}
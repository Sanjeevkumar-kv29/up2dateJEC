package com.example.up2dateJEC

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



            Handler().postDelayed(
                {
                    startActivity(Intent(this,Login_Activity::class.java))
                    overridePendingTransition(R.anim.leftout,R.anim.rightin)
                    finish()

                },3000 )
        }


}
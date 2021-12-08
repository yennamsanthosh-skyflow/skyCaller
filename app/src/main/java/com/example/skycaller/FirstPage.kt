package com.example.skycaller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()
        var h = Handler()
        h.postDelayed(
            object : Runnable{
                override fun run() {
                   startActivity(Intent(this@FirstPage,MainActivity::class.java))
                    finish()
                }

            },3000
        )
    }
}
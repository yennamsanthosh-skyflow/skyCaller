package com.example.skycaller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import android.telephony.TelephonyManager




class MainActivity : AppCompatActivity() {
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //tv1.setText(telephonyManager.getDeviceId().toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.call -> {
                startActivity(Intent(this,CallActivity::class.java))

            }
            R.id.view ->{
                startActivity(Intent(this,ViewActivity::class.java))

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
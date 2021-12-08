package com.example.skycaller

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_view.*
import kotlinx.android.synthetic.main.activity_view.*
import kotlinx.android.synthetic.main.activity_view.business
import kotlinx.android.synthetic.main.activity_view.contacts
import kotlinx.android.synthetic.main.activity_view.foreign
import okhttp3.OkHttpClient

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        edit.setOnClickListener {
            val intent = Intent(this,EditViewActivity::class.java)
            intent.putExtra("contacts",contacts.text.toString())
            intent.putExtra("business",business.text.toString())
            intent.putExtra("foreign",foreign.text.toString())
            startActivity(intent)
        }
        //sendRequest()
    }
    fun sendRequest()
    {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setMessage("please wait..")
        dialog.show()
        val database = FirebaseDatabase.getInstance().getReference("users").child("1234").child("view")
        database.addListenerForSingleValueEvent( object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val keys = snapshot.children
                    keys.forEach{

                        when (it.key) {
                            "contacts" ->{
                                contacts.setText(it.value.toString())
                            }
                            "business" -> {
                                business.setText(it.value.toString())

                            }
                            "foreign"->{
                                foreign.setText(it.value.toString())

                            }
                        }

                    }
                    dialog.dismiss()
                }
                else
                    dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        sendRequest()
    }
}

//val request = okhttp3.Request.Builder().url("url").build()
//        val okHttpClient = OkHttpClient()
//        try {
//            val thread = Thread {
//                run {
//                    okHttpClient.newCall(request).execute().use { response ->
//
//                    }
//                }
//            }
//            thread.start()
//        } catch (exception: Exception) {
//
//        }
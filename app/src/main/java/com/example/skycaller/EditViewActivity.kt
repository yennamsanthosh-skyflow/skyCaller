package com.example.skycaller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_view.*

class EditViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_view)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        val list= mutableListOf<String>()
        list.add("PLAIN-TEXT")
        list.add("REDACTED")
        list.add("MASKED")
        val contactID = list.indexOf(intent.getStringExtra("contacts"))
        val businessID = list.indexOf(intent.getStringExtra("business"))
        val foreignID = list.indexOf(intent.getStringExtra("foreign"))
        var contactRedaction = ""
        var businessRedaction = ""
        var foreignRedaction = ""



        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        contacts.setAdapter(adapter)
        business.setAdapter(adapter)
        foreign.setAdapter(adapter)

        if(contactID !=-1){
            contacts.setSelection(contactID)
        }
        if(businessID != -1){
            business.setSelection(businessID)
        }
        if(foreignID != -1){
            foreign.setSelection(foreignID)
        }
        contacts.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                contactRedaction = list.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        business.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                businessRedaction = list.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        foreign.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                foreignRedaction = list.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        done.setOnClickListener {
            sendRequest(contactRedaction,businessRedaction,foreignRedaction)
            finish()
        }

    }
    fun sendRequest(contactRedaction: String, businessRedaction: String, foreignRedaction: String)
    {
        val database = FirebaseDatabase.getInstance().getReference("users").child("1234").child("view")
        database.child("contacts").setValue(contactRedaction)
        database.child("business").setValue(businessRedaction)
        database.child("foreign").setValue(foreignRedaction)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

//        val okHttpClient = OkHttpClient()
//        val jsonBody = JSONObject()
//        val body: RequestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request
//            .Builder()
//            .method("POST", body)
//            .addHeader("Authorization", "responseBody")
//            .url("url")
//            .build()
//        try {
//            val thread = Thread {
//                run {
//                    okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
//                        override fun onFailure(call: Call, e: IOException) {
//
//                        }
//
//                        override fun onResponse(call: Call, response: Response) {
//
//                        }
//
//                    })
//                }
//            }
//            thread.start()
//        }
//        catch (exception: Exception) {
//
//        }
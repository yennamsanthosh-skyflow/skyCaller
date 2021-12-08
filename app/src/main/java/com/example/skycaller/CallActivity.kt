package com.example.skycaller

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_call.*
import kotlinx.android.synthetic.main.activity_call.business
import kotlinx.android.synthetic.main.activity_call.edit
import kotlinx.android.synthetic.main.activity_call.foreign
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class CallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        edit.setOnClickListener {
            startActivity(Intent(this,EditCallActivity::class.java))
        }

    }

    fun sendRequest()
    {
        val dialog = AlertDialog.Builder(this).create()
        dialog.setMessage("please wait..")
        dialog.show()
        val database = FirebaseDatabase.getInstance().getReference("users").child("1234").child("calls")
        database.addListenerForSingleValueEvent( object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val keys = snapshot.children
                    keys.forEach{

                        when (it.key) {
                            "businessCalls" ->{
                                if(it.value.toString().equals("true"))
                                    business.setText("NOT BLOCKED")
                                else
                                    business.setText("BLOCKED")
                            }
                            "allowedCountries" -> {
                                if(it.value.toString().equals("[]"))
                                    foreign.setText("NONE")
                                else
                                    foreign.setText(it.value.toString().substring(1,it.value.toString().length-1))
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
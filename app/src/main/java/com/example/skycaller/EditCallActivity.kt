package com.example.skycaller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_edit_call.*
import okhttp3.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase


class EditCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_call)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);

        val listOfCountries = mutableListOf<String>()
        listOfCountries.add("India")
        listOfCountries.add("USA")
        listOfCountries.add("Australia")
        listOfCountries.add("Pakistan")
        listOfCountries.add("Russia")
        listOfCountries.add("England")
        var businessCalls = false
        val selectedList = mutableListOf<String>()

        val arr = ArrayAdapter<String>(this@EditCallActivity,R.layout.support_simple_spinner_dropdown_item,
            selectedList)
        countryList.setAdapter(arr)
        bswitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked)
                    businessCalls = true
                else
                    businessCalls = false
        })
        fswitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                country_layout.visibility = View.VISIBLE
            }
            else
            {
                country_layout.visibility = View.INVISIBLE
                selectedList.clear()
                arr.notifyDataSetChanged()
            }
        })

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, listOfCountries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        country.setAdapter(adapter)



        country.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                selectedList.add(listOfCountries.get(position))
                arr.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        countryList.setOnItemClickListener(object : AdapterView.OnItemClickListener
        {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedList.removeAt(p2)
                arr.notifyDataSetChanged()
            }
        })

        country.setPrompt("select country")
        done.setOnClickListener {
            sendRequest(businessCalls,selectedList)
            finish()
        }

    }

    fun sendRequest(businessCalls: Boolean, list: MutableList<String>)
    {
       val database = FirebaseDatabase.getInstance().getReference("users").child("1234").child("calls")
        database.child("businessCalls").setValue(businessCalls)
        database.child("allowedCountries").setValue(list.toString())
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
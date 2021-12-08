package com.example.skycaller

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.DialogInterface
import android.provider.Settings

import android.widget.Toast





class MainActivity : AppCompatActivity() {
    val listPermissionsNeeded: MutableList<String> = ArrayList()
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!checkAndRequestPermissions()){
            finish()
        }
        else{
            tv1.setText(Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID))
            getContactList()
        }

    }

    private fun checkAndRequestPermissions(): Boolean {

        val contactsPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_CONTACTS)
        val phonePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        if (phonePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (contactsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("TAG", "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_CONTACTS] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.READ_CONTACTS] == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this,
                            "Done!",
                            Toast.LENGTH_LONG)
                            .show()
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_PHONE_STATE)
                        ) {
                            showDialogOK("contacts and phone Permission required for this app"
                            ) { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                    DialogInterface.BUTTON_NEGATIVE -> {}
                                }
                            }
                        } else {
                            Toast.makeText(this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG)
                                .show()
                            finish()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
//        {
//            getContactList()
//        }else{
//            finish()
//        }
//    }

//    fun readCalls()
//    {
//        var resolver: ContentResolver = contentResolver
//        var cp_uri = CallLog.Calls.CONTENT_URI
//        var c: Cursor = resolver.query(cp_uri,
//            null,
//            null,null,
//            null)!!
//        var from = arrayOf(
//            CallLog.Calls.CACHED_NAME,
//            CallLog.Calls.NUMBER,
//            CallLog.Calls.TYPE
//        )
//        var to = intArrayOf(R.id.name,R.id.number,R.id._date)
//        var cAdapter = SimpleCursorAdapter(this,
//            R.layout.indi_row,c,from,to,0)
//        lview.adapter = cAdapter
   // }
    @SuppressLint("Range")
    private fun getContactList() {
        val list = mutableListOf<String>()
        val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null)

        // Loop Through All The Numbers
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            // Cleanup the phone number
            phoneNumber = phoneNumber.replace("[()\\s-]+".toRegex(), "")
            list.add(phoneNumber)
        }

        // Get The Contents of Hash Map in Log
//        for ((key, value) in namePhoneMap.entries) {
//            Log.d("TAG", "Phone :$key")
//            Log.d("TAG", "Name :$value")
//        }
         val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, list)
        lview.adapter = adapter
        phones.close()
    }
}
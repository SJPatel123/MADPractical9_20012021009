package com.example.madpractical9_20012021009

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.madpractical9_20012021009.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lv : ListView
    private lateinit var al : ArrayList<SMSView>
    private lateinit var smsreceive : SmsBroadcastReceiver
    private val SMS_PERMISSION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lv = binding.listView
        al = ArrayList()
        if(checkRequestPermission()){
            loadSMSInbox()
        }
        smsreceive = SmsBroadcastReceiver()
        registerReceiver(smsreceive, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    private fun requestSMSPermissions(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)){

        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS),
        SMS_PERMISSION_CODE)
    }
    private val isSMSReadPermission: Boolean
        get() = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    private val isSMSWritePermission: Boolean
        get() = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    private fun checkRequestPermission(): Boolean {
        return if (!isSMSReadPermission || !isSMSWritePermission) {
            requestSMSPermissions()
            false
        } else true
    }

    private fun loadSMSInbox() {
        if (!checkRequestPermission()) return
        val uriSMS = Uri.parse("content://sms/inbox")
        val c = contentResolver.query(uriSMS, null, null, null, null)
        al.clear()
        while (c!!.moveToNext()) {
            al.add(SMSView(c.getString(2),c.getString(12)))
        }
        lv.adapter = SMSViewAdapter(this, al)
        smsreceive.listener = ListenerImplement()
    }

    inner class ListenerImplement : SmsBroadcastReceiver.Listener{
        override fun onTextReceived(sPhoneNo: String?, sMsg: String?) {
            val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("New SMS received")
            builder.setMessage("$sPhoneNo\n$sMsg")
            builder.setCancelable(true)
            builder.show()
            loadSMSInbox()
        }
    }

    fun sendSms(sPhoneNo : String, sMsg : String){
        if(!checkRequestPermission()){
            return
        }
        val smsmanager = SmsManager.getDefault()
        if(smsmanager != null){
            smsmanager.sendTextMessage(sPhoneNo, null, sMsg, null, null)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(smsreceive)
        super.onDestroy()
    }
    /*Read all sms and store in ArrayList<SMSview>
    then create object of list view and assign to
    listview object. Give manifest file permissions.
    Create requestSMSPermission method after onCreate
    method. Create LoadSMSInbox method after SMSPer.
    method.*/
}
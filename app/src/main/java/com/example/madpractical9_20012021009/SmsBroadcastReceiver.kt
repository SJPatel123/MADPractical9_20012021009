package com.example.madpractical9_20012021009

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony

class SmsBroadcastReceiver : BroadcastReceiver() {
    interface Listener{
        fun onTextReceived(sPhoneNo: String?, sMsg: String?)
    }
    private var listener : Listener? = null
    fun setListener(lis: Listener){
        listener = lis
    }
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION){
            var sPhoneNo = ""
            var sSMSBody = ""
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                for(smsMessage in  Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                    sPhoneNo = smsMessage.displayOriginatingAddress
                    sSMSBody += smsMessage.messageBody
                }
                if(listener != null){
                    listener?.onTextReceived(sPhoneNo, sSMSBody)
                }
            }
        }
    }
}
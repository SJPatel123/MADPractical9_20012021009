package com.example.madpractical9_20012021009

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.madpractical9_20012021009.databinding.SmsItemViewBinding

class SMSViewAdapter(context : Context, private val array : ArrayList<SMSView>) :
    ArrayAdapter<SMSView>(context, array.size, array){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val currentSms : SMSView? = getItem(position)
        val binding = SmsItemViewBinding.inflate(LayoutInflater.from(context))
        binding.textViewPhoneNo.text = currentSms!!.phoneNo
        binding.textViewMessage.text = currentSms!!.message
        return binding.root
    }
    }
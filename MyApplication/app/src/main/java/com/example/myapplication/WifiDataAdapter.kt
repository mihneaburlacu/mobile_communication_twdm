package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class WifiDataAdapter(context: Context, private val wifiDataList: List<WifiData>) : ArrayAdapter<WifiData>(context, android.R.layout.simple_list_item_1, wifiDataList) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = rowView.findViewById<TextView>(android.R.id.text1)
        textView.text = wifiDataList[position].ssid
        return rowView
    }
}

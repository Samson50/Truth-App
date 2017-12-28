package com.example.jacob.navdrawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.jacob.navdrawer.R
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class CommitteesAdapter(context: Context): BaseAdapter() {

    private val mContext: Context = context
    private var committees: MutableList<String> = mutableListOf()

    init {
        //mContext = context
    }

    private fun parse(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }

    fun getCommittees(fname: String,lname: String) {
        Log.d("ComAdapt",fname)
        val url = "http://10.0.2.2/api/legislator/getCommittees.php?fname=$fname&lname=$lname"
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONArray("value")
        //var holder: ImageView? = null
        for (i in 0.until(json.length())) {
            val committee = json.getJSONObject(i).getString("ComName")
            committees.add(i,committee)
        }
    }

    // Render Each Row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val article = layoutInflater.inflate(R.layout.basic_info, viewGroup, false)
        if (committees.size != 0) {
            article.findViewById<TextView>(R.id.list_item_info).text = committees[position]
        }
        else {
            article.findViewById<TextView>(R.id.list_item_info).text = "No Committees"
        }
        return article
    }

    override fun getItem(p0: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    // Responsible for rows in list
    override fun getCount(): Int {
        // Number of rows
        return when (committees.size) {
            0 -> 1
            else -> committees.size
        }
    }
}
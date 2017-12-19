package com.example.jacob.navdrawer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.jacob.navdrawer.R
import com.example.jacob.navdrawer.R.id.sponsor
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class BillsAdapter(context: Context): BaseAdapter() {

    private val mContext: Context = context
    private var names: MutableList<String> = mutableListOf()
    private var congresses: MutableList<String> = mutableListOf()
    private var sponsors: MutableList<String> = mutableListOf()
    private var descriptions: MutableList<String> = mutableListOf()
    private var policies: MutableList<String> = mutableListOf()
    private var summaries: MutableList<String> = mutableListOf()

    private var numBills: Int = 0
    private var page: Int = 0
    private var perPage: Int = 20
    private var search = ""
    private var searchParameters = ""
    //private var images: MutableList<ImageView> = mutableListOf()

    init {
        //mContext = context
    }

    fun setParams(searchString: String="", parameters:String = ""){
        if (parameters!=""){
            searchParameters = parameters
        }
        if (searchString != "") {
            search = "$searchParameters&sname=$searchString"
        }
        else {
            search = searchParameters
        }
    }

    fun updatePage(p:Int,perp:Int){
        page = p
        perPage = perp
        getBills()
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

    fun getBills() {
        val url = "http://192.168.1.72/api/bill/getBills.php?p=$page&perp=$perPage$search"
        Log.d("BILLADAPT", url)
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONArray("value")
        numBills = json.length()
        for (i in 0 until json.length()) {
            val article = json!!.getJSONObject(i)
            if (article is JSONObject) {
                Log.d("BILLS","In the thing %d".format(i))
                val name = article.getString("name")
                names.add(i,name)
                val sponsor = article.getString("FirstName")+" "+article.getString("LastName")+" ["+article.getString("party")+"-"+article.getString("state")+"]"
                Log.d("BILLS",sponsor)
                sponsors.add(i,sponsor)
                val job: String =
                    if (article.getString("job").contains("H",true)) "Representative, "
                    else "Senator, "
                Log.d("BILLS",job)
                val spon_des = job+article.getString("first")+"-Present"
                Log.d("BILLS", spon_des)
                descriptions.add(i,spon_des)
                val policy  = article.getString("PAName")
                policies.add(i,policy)
                val brief = "Summary:  "+article.getString("Summary")
                summaries.add(i,brief)
                val congr = article.getString("congress")
                congresses.add(i,congr)
                Log.d("BILLS","Last the thing %d".format(i))
            }
        }
        Log.d("BILLS","Out of the thing")
    }

    // Render Each Row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val article = layoutInflater.inflate(R.layout.info_bill, viewGroup, false)

        article.findViewById<TextView>(R.id.info_bill_con).text = congresses[position]
        article.findViewById<TextView>(R.id.info_bill_name).text = names[position]
        article.findViewById<TextView>(R.id.info_sponsor).text = sponsors[position]
        article.findViewById<TextView>(R.id.info_policy).text = policies[position]
        article.findViewById<TextView>(R.id.info_summary).text = summaries[position]
        article.findViewById<TextView>(R.id.info_sponsor_description).text = descriptions[position]
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
        return numBills
    }
}
package com.example.jacob.navdrawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/7/2017.
 */

class LegislatorsAdapter(context: Context): BaseExpandableListAdapter() {

    private val mContext: Context = context
    private var sNames: MutableList<String> = mutableListOf()
    private var hNames: MutableList<String> = mutableListOf()
    private var sDescriptions: MutableList<String> = mutableListOf()
    private var hDescriptions: MutableList<String> = mutableListOf()
    private var sImageURL: MutableList<String> = mutableListOf()
    private var hImageURL: MutableList<String> = mutableListOf()
    private var sURL: MutableList<String> = mutableListOf()
    private var hURL: MutableList<String> = mutableListOf()


    private fun parse(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            Log.d("ADAPT", "JSON Parse failed")
            e.printStackTrace()
        }
        return jsonObject
    }

    fun getLegislators(state: String) {
        val url = "http://10.0.2.2/api/legislator/getState.php?id=%27$state%27"//R.string.server.toString()+
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONArray("value")
        var hIndex = 0
        var sIndex = 0
        Log.d("ADAPT", "ALL: %d".format(json.length()))
        val length = json.length()-1
        for (i in 0..length) {
            val article = json.getJSONObject(i)
            if (article is JSONObject) {
                val name = (article.get("firstname") as String)+" "+article.get("lastname")+" ["+article.get("party")+"-$state]"
                val image = article.getString("PicURL")
                val job = (article.get("job") as String)
                if (job.contains("H",true)) {
                    hNames.add(hIndex,name)
                    hImageURL.add(hIndex,image)
                    val description = "Representative, "+(article.get("first") as String)+"-Present"
                    hDescriptions.add(hIndex,description)
                    hURL.add(hIndex,image)
                    hIndex+=1
                }
                else {
                    Log.d("ADAPT","AddingS")
                    sNames.add(sIndex,name)
                    sImageURL.add(sIndex,image)
                    val description = "Senator, "+(article.get("first") as String)+"-Present"
                    sDescriptions.add(sIndex, description)
                    sURL.add(sIndex,image)
                    sIndex+=1
                    Log.d("ADAPT","AddedS")
                }
            }
        }
        Log.d("ADAPT", "Got Out")
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        Log.d("EXADAPT", "isChildSelectable")
        return true
    }

    override fun hasStableIds(): Boolean {
        Log.d("EXADAPT", "hasStableIds")
        return  true
    }

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        Log.d("EXADAPT", "getGroupView")
        var nView = p2
        if (nView==null) {
            val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            nView = varInflate.inflate(R.layout.basic_header, null)
        }
        if (p0==0) {
            nView!!.findViewById<TextView>(R.id.list_header_text).text = "Senate"
        }
        else {
            nView!!.findViewById<TextView>(R.id.list_header_text).text = "House"
        }

        return nView
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        Log.d("EXADAPT", "getChildView")
        var nView = p3
        if (nView==null) {
            val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            nView = varInflate.inflate(R.layout.info_legislator, null)
        }
        if (p0==0) {
            nView!!.findViewById<TextView>(R.id.legislatorName).text = sNames[p1]
            nView.findViewById<TextView>(R.id.legislatorDescription).text = sDescriptions[p1]
            if (sImageURL[p1] != "none") doAsync {
                val image = BitmapFactory.decodeStream(URL(sImageURL[p1]).openConnection().getInputStream())
                uiThread { nView.findViewById<ImageView>(R.id.legislatorImage).setImageBitmap(image) }
            }
        }
        else {
            nView!!.findViewById<TextView>(R.id.legislatorName).text = hNames[p1]
            nView.findViewById<TextView>(R.id.legislatorDescription).text = hDescriptions[p1]
            if (hImageURL[p1] != "none") doAsync {
                val image = BitmapFactory.decodeStream(URL(hImageURL[p1]).openConnection().getInputStream())
                uiThread { nView.findViewById<ImageView>(R.id.legislatorImage).setImageBitmap(image) }
            }
        }
        return nView
    }


    override fun getGroupCount(): Int {
        Log.d("EXADAPT", "getGroupCount")
        return 2
    }

    override fun getChildrenCount(p0: Int): Int {
        Log.d("EXADAPT", "getChildrenCount")
        if (p0==0) {
            return 2
        }
        else {
            return hNames.size
        }
    }

    override fun getGroupId(p0: Int): Long {
        Log.d("EXADAPT", "getGroupId")
        return 0
    }

    override fun getChildId(groupPos: Int, childPos: Int): Long {
        Log.d("EXADAPT", "getChildId")
        return 0
    }



    override fun getGroup(p0: Int): Any {
        Log.d("EXADAPT", "getGroup")
        return 0
    }

    override fun getChild(p0: Int, p1: Int): Any {
        Log.d("EXADAPT", "getChild")
        return 0
    }
}
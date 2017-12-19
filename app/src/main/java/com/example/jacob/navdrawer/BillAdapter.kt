package com.example.jacob.navdrawer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/7/2017.
 */

class BillAdapter(context: Context): BaseExpandableListAdapter() {

    private val mContext: Context = context

    private var actions: MutableList<String> = mutableListOf()
    private var actionDates: MutableList<String> = mutableListOf()
    private var cosponsors: MutableList<String> = mutableListOf()
    private var descriptions: MutableList<String> = mutableListOf()
    private var committees: MutableList<String> = mutableListOf()
    private var relatedBills: MutableList<String> = mutableListOf()


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

    fun getBillDetails(name: String, congress: String) {
        val url = "http://192.168.1.72/api/bill/getBillDetails.php?name=%27$name%27&con=%27$congress%27"
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONObject("value")
        val acts = json.getJSONArray("actions")
        val cospons = json.getJSONArray("cosponsors")
        val commits = json.getJSONArray("committees")
        val relates = json.getJSONArray("related-bills")
        for (i in 0 until(acts.length())) {
            actions.add(i,acts.getJSONObject(i).getString("actionstr"))
            actionDates.add(i,acts.getJSONObject(i).getString("actiondate"))
        }
        for (i in 0 until(cospons.length())) {
            val cosName = cospons.getJSONObject(i).getString("FirstName")+" "+cospons.getJSONObject(i).getString("LastName")+" ["+cospons.getJSONObject(i).getString("party")+"-"+cospons.getJSONObject(i).getString("state")+"]"
            cosponsors.add(i,cosName)
            val job =
                    if (cospons.getJSONObject(i).getString("job").contains("H", true)) "Representative, "
                    else "Senator, "
            val description = job+cospons.getJSONObject(i).getString("first")+"-Present"
            descriptions.add(i,description)
        }
        for (i in 0 until(commits.length())) {
            committees.add(i,commits.getJSONObject(i).getString("comname"))
        }
        for (i in 0 until(relates.length())) {
            relatedBills.add(i,relates.getJSONObject(i).getString("rbname"))
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
        when (p0) {
            0 -> {
                nView!!.findViewById<TextView>(R.id.list_header_text).text = "Actions"
            }
            1 -> {
                nView!!.findViewById<TextView>(R.id.list_header_text).text = "Cosponsors"
            }
            2 -> {
                nView!!.findViewById<TextView>(R.id.list_header_text).text = "Committees"
            }
            3 -> {
                nView!!.findViewById<TextView>(R.id.list_header_text).text = "Related Bills"
            }
            // IMPLEMENT POLICY AREA
        }

        return nView!!
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        Log.d("EXADAPT", "getChildView")
        var nView = p3
        // Action, Cosponsors, Committees, Related Bills
        /*
        if (nView==null) {
            if (p0==0){
                // Inflate for action: date
                val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                nView = varInflate.inflate(R.layout.info_action, null)
            }
            else if (p0==1) {
                val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                nView = varInflate.inflate(R.layout.basic_legislator, null)
            }
            else {
                // Inflate for single info
                val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                nView = varInflate.inflate(R.layout.basic_info, null)
            }
        }*/
         if (p0==0){
            val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            nView = varInflate.inflate(R.layout.info_action, null)

        }
        else if (p0==1) {
            val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            nView = varInflate.inflate(R.layout.basic_legislator, null)
        }
        else {
            val varInflate: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            nView = varInflate.inflate(R.layout.basic_info, null)
        }

        when (p0) {
            0 -> { //Action
                when (actions.size){
                    0 -> {
                        nView!!.findViewById<TextView>(R.id.action_name).text = mContext.getString(R.string.no_action)
                        nView.findViewById<TextView>(R.id.action_date).text = mContext.getString(R.string.no_action_date)
                    }
                    else -> {
                        nView!!.findViewById<TextView>(R.id.action_name).text = actions[p1]
                        nView.findViewById<TextView>(R.id.action_date).text = actionDates[p1]
                    }
                }

            }
            1 -> {
                when (cosponsors.size) {
                    0 -> {
                        nView!!.findViewById<TextView>(R.id.basic_sponsor).text = mContext.getString(R.string.no_info)
                        nView.findViewById<TextView>(R.id.basic_description).text = mContext.getString(R.string.no_info)
                    }
                    else -> {
                        nView!!.findViewById<TextView>(R.id.basic_sponsor).text = cosponsors[p1]
                        nView.findViewById<TextView>(R.id.basic_description).text = descriptions[p1]
                    }
                }
            }
            2 -> {
                when (committees.size) {
                    0 -> nView!!.findViewById<TextView>(R.id.list_item_info).text = mContext.getString(R.string.no_info)
                    else -> nView!!.findViewById<TextView>(R.id.list_item_info).text = committees[p1]
                }
            }
            3 -> {
                when (relatedBills.size) {
                    0 -> nView!!.findViewById<TextView>(R.id.list_item_info).text = mContext.getString(R.string.no_info)
                    else -> nView!!.findViewById<TextView>(R.id.list_item_info).text = relatedBills[p1]
                }
            }
        // IMPLEMENT POLICY AREA
        }
        return nView!!
    }


    override fun getGroupCount(): Int {
        Log.d("EXADAPT", "getGroupCount")
        return 4
    }

    override fun getChildrenCount(p0: Int): Int {
        Log.d("EXADAPT", "getChildrenCount")
        when (p0) {
            0 -> { //Action
                return when (actions.size) {
                    0 ->  1
                    else ->  actions.size
                }
            }
            1 -> {
                return when (cosponsors.size) {
                    0 ->  1
                    else ->  cosponsors.size
                }
            }
            2 -> {
                return when (committees.size) {
                    0 ->  1
                    else ->  committees.size
                }            }
            3 -> {
                return when (relatedBills.size) {
                    0 ->  1
                    else ->  relatedBills.size
                }            }
            else -> {
                return 0
            }
        // IMPLEMENT POLICY AREA
        }
    }

    override fun getGroupId(p0: Int): Long {
        Log.d("EXADAPT", "getGroupId")
        return p0.toLong()
    }

    override fun getChildId(groupPos: Int, childPos: Int): Long {
        Log.d("EXADAPT", "getChildId")
        return childPos.toLong()
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
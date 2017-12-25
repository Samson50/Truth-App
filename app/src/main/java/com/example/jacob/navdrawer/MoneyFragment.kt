package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/4/2017.
 */

class MoneyFragment : Fragment(), View.OnClickListener {

    private val TAG = "Fragment 10"

    private var contributorNames = mutableListOf<String>()
    private var contributorTotals = mutableListOf<String>()
    private var contributorPACs = mutableListOf<String>()
    private var CID = ""
    private var image: Bitmap? = null
    //var cont: Context? = null

    companion object {
        private val ARG_NAME = "NAME"
        private val ARG_DESC = "DESC"
        private val ARG_URL = "URL"
        fun newInstance(name:String="default",description: String="default",imageURL: String="none"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, description)
            args.putString(ARG_URL, imageURL)
            val fragment = MoneyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getImage() {
        doAsync {
            if (arguments[ARG_URL] != "none") image = BitmapFactory.decodeStream(URL(arguments[ARG_URL] as String).openConnection().getInputStream())
            uiThread {
                Log.d("VOTES", "In UI Thread")
                if (image != null) view!!.findViewById<ImageView>(R.id.legislatorImage)
            }
        }
    }

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

    private fun fillContributions(organizations: View, industries: View){
        organizations.findViewById<TextView>(R.id.contributorName1).text = contributorNames[0]
        organizations.findViewById<TextView>(R.id.contributorPAC1).text = contributorPACs[0]
        organizations.findViewById<TextView>(R.id.contributorTotal1).text = contributorTotals[0]
        organizations.findViewById<TextView>(R.id.contributorName2).text = contributorNames[1]
        organizations.findViewById<TextView>(R.id.contributorPAC2).text = contributorPACs[1]
        organizations.findViewById<TextView>(R.id.contributorTotal2).text = contributorTotals[1]
        organizations.findViewById<TextView>(R.id.contributorName3).text = contributorNames[2]
        organizations.findViewById<TextView>(R.id.contributorPAC3).text = contributorPACs[2]
        organizations.findViewById<TextView>(R.id.contributorTotal3).text = contributorTotals[2]
        organizations.findViewById<TextView>(R.id.contributorName4).text = contributorNames[3]
        organizations.findViewById<TextView>(R.id.contributorPAC4).text = contributorPACs[3]
        organizations.findViewById<TextView>(R.id.contributorTotal4).text = contributorTotals[3]
        organizations.findViewById<TextView>(R.id.contributorName5).text = contributorNames[4]
        organizations.findViewById<TextView>(R.id.contributorPAC5).text = contributorPACs[4]
        organizations.findViewById<TextView>(R.id.contributorTotal5).text = contributorTotals[4]
        industries.findViewById<TextView>(R.id.contributorName1).text = contributorNames[5]
        industries.findViewById<TextView>(R.id.contributorPAC1).text = contributorPACs[5]
        industries.findViewById<TextView>(R.id.contributorTotal1).text = contributorTotals[5]
        industries.findViewById<TextView>(R.id.contributorName2).text = contributorNames[6]
        industries.findViewById<TextView>(R.id.contributorPAC2).text = contributorPACs[6]
        industries.findViewById<TextView>(R.id.contributorTotal2).text = contributorTotals[6]
        industries.findViewById<TextView>(R.id.contributorName3).text = contributorNames[7]
        industries.findViewById<TextView>(R.id.contributorPAC3).text = contributorPACs[7]
        industries.findViewById<TextView>(R.id.contributorTotal3).text = contributorTotals[7]
        industries.findViewById<TextView>(R.id.contributorName4).text = contributorNames[8]
        industries.findViewById<TextView>(R.id.contributorPAC4).text = contributorPACs[8]
        industries.findViewById<TextView>(R.id.contributorTotal4).text = contributorTotals[8]
        industries.findViewById<TextView>(R.id.contributorName5).text = contributorNames[9]
        industries.findViewById<TextView>(R.id.contributorPAC5).text = contributorPACs[9]
        industries.findViewById<TextView>(R.id.contributorTotal5).text = contributorTotals[9]

    }

    fun getCID(fname: String, lname:String){
        val url = R.string.server.toString()+"/api/legislator/getCID.php?fname="+fname+"&lname="+lname.replace(" ","%20")
        Log.d("VOTES", url)
        val result = URL(url).readText()
        CID = JSONArray(result).getJSONObject(0).getString("CID")

    }

    private fun getContributions(fname:String,lname:String) {
        Log.d(TAG,fname)
        Log.d(TAG,lname)
        val url = "http://192.168.1.72/api/legislator/getContributions.php?fname=$fname&lname="+lname.replace(" ","%20")
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONArray("value")
        Log.d(TAG, "Items: %d".format(json.length()))
        for (i in 0 until(10)) {
            Log.d(TAG, "%d".format(i))
            val total = json.getJSONObject(i*2)
            val pac = json.getJSONObject(i*2+1)
            contributorNames.add(i,total.getString("Name"))
            contributorPACs.add(i,pac.getString("Amount"))
            contributorTotals.add(i,total.getString("Amount"))
        }

    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.moneyMoreInfo -> {
                //Do web view
                val url = "https://www.opensecrets.org/members-of-congress/summary?cid=N"+CID.padStart(8,'0')+"&cycle=2018&type=C"
                val fragment = WebFragment.newInstance(url)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
            }
        }
    }

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.fragment_contributions, container, false)
        rootView.findViewById<TextView>(R.id.legislatorName).text = arguments[ARG_NAME] as String
        rootView.findViewById<TextView>(R.id.legislatorDescription).text = arguments[ARG_DESC] as String
        rootView.findViewById<Button>(R.id.moneyMoreInfo).setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val organizations = view!!.findViewById<ConstraintLayout>(R.id.contributor1)
        val industries = view!!.findViewById<ConstraintLayout>(R.id.contributor2)
        doAsync {
            val name = arguments[ARG_NAME] as String
            val fname = (name).split(' ')[0]
            val lname = (name).split(' ').slice(IntRange(1,name.split(' ').size-2)).reduce{fn, next -> fn+" "+next}
            getCID(fname,lname)
            getContributions(fname, lname)
            uiThread {
                Log.d("VOTES", "In UI Thread")
                fillContributions(organizations, industries)
            }
        }
        getImage()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
    }

}
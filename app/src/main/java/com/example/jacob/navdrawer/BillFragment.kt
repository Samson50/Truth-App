package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/4/2017.
 */

class BillFragment : Fragment(), View.OnClickListener, ExpandableListView.OnChildClickListener {

    private val TAG = "Fragment 4"
    //private var toggler: ExpandableRelativeLayout? = null
    //var cont: Context? = null

    companion object {
        private val ARG_NAME = "BILL_NAME"
        private val ARG_CONG = "CONGRESS"
        private val ARG_SPON = "SPONSOR"
        private val ARG_SPOD = "SDESC"
        private val ARG_SUMM = "SUMMARY"
        fun newInstance(billName: String, congress: String, sponsor: String="default", sponsor_description: String="default", summary: String="none"):Fragment {//newInstance(state: String):
            val args = Bundle()
            args.putString(ARG_NAME, billName)
            args.putString(ARG_CONG, congress)
            args.putString(ARG_SPON, sponsor)
            args.putString(ARG_SUMM, summary)
            args.putString(ARG_SPOD, sponsor_description)
            val fragment = BillFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onChildClick(parent: ExpandableListView?, view: View?, groupPos: Int, childPos: Int, id: Long): Boolean {
        when(groupPos) {
            1 -> {
                val name = view!!.findViewById<TextView>(R.id.basic_sponsor).text.toString()
                val description = view.findViewById<TextView>(R.id.basic_description).text.toString()
                val fragment = LegislatorFragment.newInstance(name, description)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
                return true
            }
            3 -> {
                val name = view!!.findViewById<TextView>(R.id.list_item_info).text.toString()
                val congress = arguments[ARG_CONG] as String
                val fragment = BillFragment.newInstance(name, congress)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
                return true
            }
        }
        return false
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.sponsor -> {
                val name = view.findViewById<TextView>(R.id.sponsor).text.toString()
                val description = arguments[ARG_SPOD].toString()
                val fragment = LegislatorFragment.newInstance(name, description)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
            }
        }
        //Log.d("BILL","Bill Clicker %d".format(p0.id))
    }

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
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

    private fun getInfo() {
        val url = "http://192.168.1.72/api/bill/getInfo.php?name=%27"+arguments[ARG_NAME]+"%27&con=%27"+arguments[ARG_CONG]+"%27"
        val result = URL(url).readText()
        val article = parse(result)!!.getJSONArray("value").getJSONObject(0)
        val sponsor = article.getString("FirstName")+" "+article.getString("LastName")+" ["+article.getString("party")+"-"+article.getString("state")+"]"
        Log.d("VOTES",sponsor)
        arguments.putString(ARG_SPON,sponsor)//= sponsor
        val job: String =
                if (article.getString("job").contains("H",true)) "Representative, "
                else "Senator, "
        Log.d("VOTES",job)
        val spon_des = job+article.getString("first")+"-Present"
        Log.d("VOTES", spon_des)
        arguments.putString(ARG_SPOD,spon_des)
        val summary = article.getString("summary")
        arguments.putString(ARG_SUMM,summary)
    }

    private fun updateInfo() {
        view!!.findViewById<TextView>(R.id.sponsor).text = arguments[ARG_SPON] as String
        view!!.findViewById<TextView>(R.id.sponsor_description).text = arguments[ARG_SPOD] as String
        view!!.findViewById<TextView>(R.id.bill_title).text = arguments[ARG_SUMM] as String
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.fragment_bill, container, false)
        rootView.findViewById<TextView>(R.id.bill_name).text = arguments[ARG_NAME] as String
        rootView.findViewById<TextView>(R.id.sponsor).text = arguments[ARG_SPON] as String
        rootView.findViewById<TextView>(R.id.bill_title).text = arguments[ARG_SUMM] as String
        rootView.findViewById<TextView>(R.id.sponsor_description).text = arguments[ARG_SPOD] as String
        rootView.findViewById<ExpandableListView>(R.id.bill_details).setOnChildClickListener(this)
        rootView.findViewById<TextView>(R.id.sponsor).setOnClickListener(this)
        if (arguments[ARG_SPON] as String == "default"){
            doAsync {
                getInfo()
                uiThread {
                    updateInfo()
                }
            }
        }
        //val voteList = rootView.findViewById<ListView>(R.id.legis_votes)
        //voteList.setOnClickListener(this)
        //val toggleButton = rootView.findViewById<TextView>(R.id.expand_search)
        //toggler = rootView.findViewById(R.id.search_items)
        //toggleButton.setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = BillAdapter(activity)
        val billDeets = view!!.findViewById<ExpandableListView>(R.id.bill_details)
        doAsync {
            adapter.getBillDetails(arguments[ARG_NAME].toString(), arguments[ARG_CONG].toString())
            Log.d("BillAD","Finished Async")
            uiThread {
                //Log.d("LegisList", "In UI Thread")
                billDeets.setAdapter(adapter)
            }
        }
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
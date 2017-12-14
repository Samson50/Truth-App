package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

/**
 * Created by Jacob on 12/4/2017.
 */

class BillsListFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener {

    private val filterFragment = FilterFragment.newInstance()
    private val TAG = "Fragment 7"
    private var page = 0
    //var cont: Context? = null

    companion object {
        //private val ARG_STATE = "STATE"
        fun newInstance():Fragment {//newInstance(state: String):
            //val args: Bundle = Bundle()
            //args.putSerializeable(ARG_STATE, state)
            //val fragment = BillsListFragment.newInstance()
            //fragment.arguments = args
            //return fragment
            return BillsListFragment()
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.expand_search -> {
                val curfrag = childFragmentManager.findFragmentByTag("FILTER")
                if (curfrag == null) {
                    childFragmentManager.beginTransaction().replace(R.id.search_items, filterFragment, "FILTER").commit()
                    Log.d(TAG, "TEST")
                }
                else {
                    childFragmentManager.beginTransaction().remove(curfrag).commit()
                }
            }
            R.id.search_back -> {
                if (page > 0) page -= 1
            }
            R.id.search_next -> {
                page += 1
            }
        }
    }

    //p0: List    p1: List item clicked    p2: Index of clicked item
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // List Item index is p2
        val exText = p1!!.findViewById<TextView>(R.id.info_bill_name).text.toString()
        val sponsor = p1.findViewById<TextView>(R.id.info_sponsor).text.toString()
        val sponsor_description = p1.findViewById<TextView>(R.id.info_sponsor_description).text.toString()
        val summary = p1.findViewById<TextView>(R.id.info_summary).text.toString()
        val congress = p1.findViewById<TextView>(R.id.info_bill_con).text.toString()
        val fragment = BillFragment.newInstance(exText,congress,sponsor,sponsor_description,summary)
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        Log.d("BILLS", exText)//"Vote Clicker %d".format(p2))
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
        val rootView = inflater!!.inflate(R.layout.fragment_list_bills, container, false)
        rootView.findViewById<ListView>(R.id.bills_found).setOnItemClickListener(this)
        val toggleButton = rootView.findViewById<TextView>(R.id.expand_search)
        toggleButton.setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = BillsAdapter(activity)
        val billsList = view!!.findViewById<ListView>(R.id.bills_found)
        doAsync {
            adapter.getBills(114)
            Log.d("LegisList","Finished Async")
            uiThread {
                Log.d("LegisList", "In UI Thread")
                billsList.adapter = adapter
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
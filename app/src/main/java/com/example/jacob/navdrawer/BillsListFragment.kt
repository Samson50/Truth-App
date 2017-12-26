package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

/**
 * Created by Jacob on 12/4/2017.
 */

class BillsListFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    private val filterFragment = FilterFragment.newInstance("bills")
    private val TAG = "Fragment 7"
    private var page = 0
    private var perPage = 20
    private var searchString: String = ""
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

    fun setSearch(congress:String,bills:Boolean,amendments:Boolean,resolutions:Boolean,house:Boolean,senate:Boolean){
        Log.d(TAG, "In searcher")
        var search = ""
        if (congress != "ALL") search += "&congress=$congress"
        if (!(bills && amendments && resolutions && house && senate)){
            search += "&params="
            if (bills) search += '1'
            else search += '0'
            if (amendments) search += '1'
            else search += '0'
            if (resolutions) search += '1'
            else search += '0'
            if (house) search += '1'
            else search += '0'
            if (senate) search += '1'
            else search += '0'
        }
        val billsAdapter = view!!.findViewById<ListView>(R.id.bills_found).adapter as BillsAdapter
        billsAdapter.setParams(parameters=search)
        doAsync {
            billsAdapter.getBills()
            uiThread {
                billsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        Log.d("BLF", "key = %d".format(p1))//p2!!.keyCode == KeyEvent.KEYCODE_ENTER
        if (p1 == 5) {
            Log.d("BLF","In the thing")
            searchString = p0!!.text.toString()
            val billsAdapter = view!!.findViewById<ListView>(R.id.bills_found).adapter as BillsAdapter
            billsAdapter.setParams(searchString)
            doAsync {
                billsAdapter.getBills()
                uiThread {
                    billsAdapter.notifyDataSetChanged()
                }
            }
            return true
        }
        return false
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
                if (page > 0) {
                    page -= 1
                    val adapter = view!!.findViewById<ListView>(R.id.bills_found).adapter as BillsAdapter
                    doAsync {
                        adapter.updatePage(page,perPage)
                        uiThread {
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            R.id.search_next -> {
                page += 1
                val adapter = view!!.findViewById<ListView>(R.id.bills_found).adapter as BillsAdapter
                doAsync {
                    adapter.updatePage(page,perPage)
                    uiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    //p0: List    p1: List item clicked    p2: Index of clicked item
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // List Item index is p2
        val exText = p1!!.findViewById<TextView>(R.id.info_bill_name).text.toString()
        val sponsor = p1.findViewById<TextView>(R.id.info_sponsor).text.toString()
        val sponsorDescription = p1.findViewById<TextView>(R.id.info_sponsor_description).text.toString()
        val summary = p1.findViewById<TextView>(R.id.info_summary).text.toString()
        val congress = p1.findViewById<TextView>(R.id.info_bill_con).text.toString()
        val fragment = BillFragment.newInstance(exText,congress,sponsor,sponsorDescription,summary)
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
        (activity as NavDrawerActivity).setTitle("Search Bills")
        val rootView = inflater!!.inflate(R.layout.fragment_list_bills, container, false)
        rootView.findViewById<ListView>(R.id.bills_found).onItemClickListener = this//setOnItemClickListener(this)
        rootView.findViewById<TextView>(R.id.expand_search).setOnClickListener(this)
        rootView.findViewById<TextView>(R.id.search_next).setOnClickListener(this)
        rootView.findViewById<TextView>(R.id.search_back).setOnClickListener(this)
        rootView.findViewById<EditText>(R.id.search_option).setOnEditorActionListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = BillsAdapter(activity)
        val billsList = view!!.findViewById<ListView>(R.id.bills_found)
        doAsync {
            adapter.getBills()
            Log.d("BillsList","Finished Async")
            uiThread {
                Log.d("BillsList", "In UI Thread")
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
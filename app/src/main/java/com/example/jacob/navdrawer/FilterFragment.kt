package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Jacob on 12/4/2017.
 */

class FilterFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val TAG = "Fragment 3"
    private val congresses = arrayOf("114","113")
    private lateinit var option: Spinner

    private var congress = "ALL"
    private var bills = true
    private var amendbments = true
    private var resolutions = true
    private var house = true
    private var senate = true

    //private var toggler: ExpandableRelativeLayout? = null
    //var cont: Context? = null

    companion object {
        private val ARG_PARENT = "PARENT"
        fun newInstance(parent: String):Fragment {//newInstance(caught: Pokemon):
            val args = Bundle()
            args.putString(ARG_PARENT, parent)
            val fragment = FilterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(TAG, "Nothing Selected")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        congress = congresses[p2]
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.buttonApply -> {
                bills = view!!.findViewById<CheckBox>(R.id.checkBills).isChecked
                amendbments = view!!.findViewById<CheckBox>(R.id.checkAmendments).isChecked
                resolutions = view!!.findViewById<CheckBox>(R.id.checkResolutions).isChecked
                house = view!!.findViewById<CheckBox>(R.id.checkHouse).isChecked
                senate = view!!.findViewById<CheckBox>(R.id.checkSenate).isChecked
                if (arguments[ARG_PARENT] == "votes") {
                    val parent = parentFragment as VotingRecordFragment
                    parent.setSearch(congress,bills,amendbments,resolutions,house,senate)
                }
                else {
                    val parent = parentFragment as BillsListFragment
                    parent.setSearch(congress,bills,amendbments,resolutions,house,senate)
                }
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
        val rootView = inflater!!.inflate(R.layout.search_layout, container, false)
        option = rootView.findViewById(R.id.spinnerCongress)
        option.adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,congresses)
        option.onItemSelectedListener = this
        rootView.findViewById<ImageView>(R.id.buttonApply).setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
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
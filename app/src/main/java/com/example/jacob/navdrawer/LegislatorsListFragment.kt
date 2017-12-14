package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text

/**
 * Created by Jacob on 12/4/2017.
 */

class LegislatorsListFragment : Fragment(), ExpandableListView.OnChildClickListener {

    private val TAG = "Fragment 6"
    //var cont: Context? = null

    companion object {
        private val ARG_STATE = "STATE"
        fun newInstance(state: String):Fragment {//newInstance(state: String):
            val args = Bundle()
            args.putString(ARG_STATE, state)
            val fragment = LegislatorsListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onChildClick(parent: ExpandableListView?, view: View?, groupPos: Int, childPos: Int, id: Long): Boolean {
        val name = view!!.findViewById<TextView>(R.id.legislatorName).text.toString()
        val description = view.findViewById<TextView>(R.id.legislatorDescription).text.toString()
        val fragment = LegislatorFragment.newInstance(name, description)
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        //p1!!.findViewById<View>(p2).findViewById<TextView>(R.id.articleHeadline).text.toString()
        Log.d("VOTES", name)//"Vote Clicker %d".format(p2))
        return true
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
        val rootView = inflater!!.inflate(R.layout.fragment_list_legislators, container, false)
        val expandView = rootView.findViewById<ExpandableListView>(R.id.legislators)
        //toggler = rootView.findViewById(R.id.search_items)
        expandView.setOnChildClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = LegislatorsAdapter(activity)
        val legisView = view!!.findViewById<ExpandableListView>(R.id.legislators)
        doAsync {
            adapter.getLegislators(arguments[ARG_STATE].toString())
            //Log.d("LegisList","Finished Async")
            uiThread {
                //Log.d("LegisList", "In UI Thread")
                legisView.setAdapter(adapter)
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
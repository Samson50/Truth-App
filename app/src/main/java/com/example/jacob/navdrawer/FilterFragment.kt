package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Jacob on 12/4/2017.
 */

class FilterFragment : Fragment(), View.OnClickListener {

    private val TAG = "Fragment 3"
    //private var toggler: ExpandableRelativeLayout? = null
    //var cont: Context? = null

    companion object {
        //private val ARG_CAUGHT = "asdfadsf"
        fun newInstance():Fragment {//newInstance(caught: Pokemon):
            //val args: Bundle = Bundle()
            //args.putSerializeable(ARG_CAUGHT, caught)
            //val fragment = FirstFragment()
            //fragment.arguments = args
            //return fragment
            return FilterFragment()
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.expand_search -> {
                //childFragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
                //toggler!!.toggle()
                Log.d(TAG, "TEST")
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
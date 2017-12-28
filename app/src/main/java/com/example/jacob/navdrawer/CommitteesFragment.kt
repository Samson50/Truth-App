package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/4/2017.
 */

class CommitteesFragment : Fragment() {

    private val TAG = "CommitteesFrag"
    //var cont: Context? = null

    companion object {
        private val ARG_FNAME = "FNAME"
        private val ARG_LNAME = "LNAME"
        fun newInstance(fname:String="default",lname:String="default"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_FNAME, fname)
            args.putString(ARG_LNAME, lname)
            val fragment = CommitteesFragment()
            fragment.arguments = args
            return fragment
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
        (activity as NavDrawerActivity).setTitle("Legislator Profile")
        val rootView = inflater!!.inflate(R.layout.fragment_news_feed, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = CommitteesAdapter(activity)
        val committeesView = view!!.findViewById<ListView>(R.id.newsFeed)
        doAsync { adapter.getCommittees(arguments[ARG_FNAME].toString(),arguments[ARG_LNAME].toString())
            uiThread { committeesView.adapter = adapter }
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
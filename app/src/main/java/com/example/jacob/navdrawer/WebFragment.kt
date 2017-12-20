package com.example.jacob.navdrawer

import android.content.ContentValues.TAG
import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ExpandableListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/4/2017.
 */

class WebFragment : Fragment() {

    private val TAG = "WVFrag 4"
    //private var toggler: ExpandableRelativeLayout? = null
    //var cont: Context? = null

    companion object {
        private val ARG_LINK = "LINK"

        fun newInstance(link: String):Fragment {//newInstance(state: String):
            val args = Bundle()
            args.putString(ARG_LINK, link)
            val fragment = WebFragment()
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
        val rootView = inflater!!.inflate(R.layout.fragment_web_view, container, false)
        rootView.findViewById<WebView>(R.id.webView).loadUrl(arguments[ARG_LINK].toString())
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
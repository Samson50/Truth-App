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

class NewsFeedFragment : Fragment(), AdapterView.OnItemClickListener {

    private val TAG = "NewsFrag"
    //var cont: Context? = null

    companion object {
        private val ARG_SEARCH = "SEARCH"
        fun newInstance(search:String="default"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_SEARCH, search)
            val fragment = NewsFeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val link = p1!!.findViewById<TextView>(R.id.hyperLink).text.toString()
        val parent = parentFragment as LegislatorFragment
        parent.startWebView(link)
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
        rootView.findViewById<ListView>(R.id.newsFeed).onItemClickListener = this
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = NewsFeedsAdapter(activity)
        val newsView = view!!.findViewById<ListView>(R.id.newsFeed)
        doAsync { adapter.getArticles(arguments[ARG_SEARCH].toString())
            uiThread { newsView.adapter = adapter }
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
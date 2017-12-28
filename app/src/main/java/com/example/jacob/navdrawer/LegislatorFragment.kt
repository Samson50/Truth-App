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

class LegislatorFragment : Fragment(), View.OnClickListener {

    private var imageURL = ""
    private var image: Bitmap? = null
    private val TAG = "Fragment 1"
    private var fname = "none"
    private var lname = "none"
    //var cont: Context? = null

    companion object {
        private val ARG_NAME = "NAME"
        private val ARG_DESC = "DESC"
        fun newInstance(name:String="default",description: String="default"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, description)
            val fragment = LegislatorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getImage() {
        val name = arguments[ARG_NAME] as String
        fname = (name).split(' ')[0]
        lname = (name).split(' ').slice(IntRange(1,name.split(' ').size-2)).reduce{fn, next -> fn+" "+next}
        val url = "http://10.0.2.2/api/legislator/getPicURL.php?fname=$fname&lname=$lname" //R.string.server.toString()+
        imageURL = JSONArray(URL(url).readText()).getJSONObject(0).getString("PicURL")
        Log.d(TAG, "imageURL %s".format(imageURL))
        if (imageURL != "none") image = BitmapFactory.decodeStream(URL(imageURL).openConnection().getInputStream())
    }

    fun startWebView(link:String){
        val fragment = WebFragment.newInstance(link)
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.votingRecord -> {
                val name = arguments[ARG_NAME] as String
                val desc = arguments[ARG_DESC] as String
                val fragment = VotingRecordFragment.newInstance(name, desc, imageURL)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
            }
            R.id.followTheMoney -> {
                val name = arguments[ARG_NAME] as String
                val desc = arguments[ARG_DESC] as String
                val fragment = MoneyFragment.newInstance(name, desc, imageURL)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
            }
            R.id.news_roller -> {
                Log.d(TAG, "IN THE News")
                val fragment = NewsFeedFragment.newInstance(arguments[ARG_NAME] as String)
                childFragmentManager.beginTransaction().replace(R.id.roller_layout, fragment, "FILTER").commit()
            }
            R.id.committees_roller -> {
                Log.d(TAG,"IN THE Committee")
                val fragment = CommitteesFragment.newInstance(fname, lname)
                childFragmentManager.beginTransaction().replace(R.id.roller_layout, fragment, "FILTER").commit()
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
        (activity as NavDrawerActivity).setTitle("Legislator Profile")
        val rootView = inflater!!.inflate(R.layout.fragment_legislator_profile, container, false)
        rootView.findViewById<TextView>(R.id.legislatorName).text = arguments[ARG_NAME] as String
        rootView.findViewById<TextView>(R.id.legislatorDescription).text = arguments[ARG_DESC] as String
        rootView.findViewById<TextView>(R.id.news_roller).setOnClickListener(this)
        rootView.findViewById<TextView>(R.id.committees_roller).setOnClickListener(this)
        rootView.findViewById<Button>(R.id.votingRecord).setOnClickListener(this)
        rootView.findViewById<Button>(R.id.followTheMoney).setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val name = arguments[ARG_NAME] as String
        val rollerFragment = NewsFeedFragment.newInstance(name)
        childFragmentManager.beginTransaction().replace(R.id.roller_layout, rollerFragment, "FILTER").commit()
        doAsync { getImage()
            uiThread { if (image != null) view!!.findViewById<ImageView>(R.id.legislatorImage).setImageBitmap(image) }
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
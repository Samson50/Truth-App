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

class LegislatorFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener {

    private var imageURL = ""
    private var image: Bitmap? = null
    private val TAG = "Fragment 1"
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
        val fname = (name).split(' ')[0]
        val lname = (name).split(' ').slice(IntRange(1,name.split(' ').size-2)).reduce{fn, next -> fn+" "+next}
        val url = "http://10.0.2.2/api/legislator/getPicURL.php?fname=$fname&lname=$lname" //R.string.server.toString()+
        imageURL = JSONArray(URL(url).readText()).getJSONObject(0).getString("PicURL")
        Log.d(TAG, "imageURL %s".format(imageURL))
        if (imageURL != "none") image = BitmapFactory.decodeStream(URL(imageURL).openConnection().getInputStream())
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val link = p1!!.findViewById<TextView>(R.id.hyperLink).text.toString()
        val fragment = WebFragment.newInstance(link)
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        Log.d("Legislator", "clicked %d".format(p2))//"Vote Clicker %d".format(p2))
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
        rootView.findViewById<ListView>(R.id.newsFeed).onItemClickListener = this
        rootView.findViewById<Button>(R.id.votingRecord).setOnClickListener(this)
        rootView.findViewById<Button>(R.id.followTheMoney).setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = NewsFeedsAdapter(activity)
        val newsView = view!!.findViewById<ListView>(R.id.newsFeed)
        val name = arguments[ARG_NAME] as String
        doAsync { adapter.getArticles(name)
            uiThread { newsView.adapter = adapter }
        }
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
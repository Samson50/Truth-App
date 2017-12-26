package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

/**
 * Created by Jacob on 12/4/2017.
 */

class VotingRecordFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    private val TAG = "Fragment 3"
    private val filterFragment = FilterFragment.newInstance("votes")
    private var page = 0
    private var perPage = 20
    private var searchString = ""
    private var image: Bitmap? = null
    //var cont: Context? = null

    companion object {
        private val ARG_NAME = "NAME"
        private val ARG_DESC = "DESC"
        private val ARG_URL = "URL"
        fun newInstance(name:String="default",description: String="default",imageURL:String="none"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, description)
            args.putString(ARG_URL, imageURL)
            val fragment = VotingRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getImage() {
        doAsync {
            if (arguments[ARG_URL] != "none") image = BitmapFactory.decodeStream(URL(arguments[ARG_URL] as String).openConnection().getInputStream())
            uiThread {
                Log.d("VOTES", "In UI Thread")
                if (image != null) view!!.findViewById<ImageView>(R.id.legislatorImage)
            }
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
        val votesAdapter = view!!.findViewById<ListView>(R.id.legis_votes).adapter as VotesAdapter
        votesAdapter.setParams(searchString, search)
        doAsync {
            votesAdapter.getVotes()
            uiThread {
                votesAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        Log.d("VRF", "key = %d".format(p1))//p2!!.keyCode == KeyEvent.KEYCODE_ENTER
        if (p1 == 5) {
            Log.d("VRF","In the thing")
            searchString = p0!!.text.toString()
            val votesAdapter = view!!.findViewById<ListView>(R.id.legis_votes).adapter as VotesAdapter
            votesAdapter.setParams(searchString)
            doAsync {
                votesAdapter.getVotes()
                uiThread {
                    votesAdapter.notifyDataSetChanged()
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
                    val adapter = view!!.findViewById<ListView>(R.id.legis_votes).adapter as VotesAdapter
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
                val adapter = view!!.findViewById<ListView>(R.id.legis_votes).adapter as VotesAdapter
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
        val billName = p1!!.findViewById<TextView>(R.id.vote_bill).text.toString()
        val billCongress = p1.findViewById<TextView>(R.id.vote_congress).text.toString()
        val fragment = BillFragment.newInstance(billName=billName,congress=billCongress)
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        //p1!!.findViewById<View>(p2).findViewById<TextView>(R.id.articleHeadline).text.toString()
        Log.d("VOTES", "clicked %d".format(p2))//"Vote Clicker %d".format(p2))
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
        (activity as NavDrawerActivity).setTitle("Voting Record")
        val rootView = inflater!!.inflate(R.layout.fragment_voting_record, container, false)
        rootView.findViewById<TextView>(R.id.legislatorName).text = arguments[ARG_NAME] as String
        rootView.findViewById<TextView>(R.id.legislatorDescription).text = arguments[ARG_DESC] as String
        rootView.findViewById<ListView>(R.id.legis_votes).onItemClickListener = this//setOnItemClickListener(this)
        //votes_list.adapter =
        rootView.findViewById<EditText>(R.id.search_option).setOnEditorActionListener(this)
        rootView.findViewById<TextView>(R.id.expand_search).setOnClickListener(this)
        rootView.findViewById<TextView>(R.id.search_next).setOnClickListener(this)
        rootView.findViewById<TextView>(R.id.search_back).setOnClickListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val votesList = view!!.findViewById<ListView>(R.id.legis_votes)
        val voteAdapter = VotesAdapter(context)
        val name = arguments[ARG_NAME] as String
        val fname = (name).split(' ')[0]
        val lname = (name).split(' ').slice(IntRange(1,name.split(' ').size-2)).reduce{fn, next -> fn+" "+next}
        doAsync {
            Log.d("VOTES",fname)
            Log.d("VOTES", lname)
            voteAdapter.getVotes(fname,lname)
            uiThread {
                Log.d("VOTES", "In UI Thread")
                votesList.adapter = voteAdapter
            }
        }
        getImage()
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
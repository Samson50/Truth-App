package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Jacob on 12/4/2017.
 */

class VotingRecordFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener {

    private val TAG = "Fragment 3"
    private val filterFragment = FilterFragment.newInstance()
    //var cont: Context? = null

    companion object {
        private val ARG_NAME = "NAME"
        private val ARG_DESC = "DESC"
        fun newInstance(name:String="default",description: String="default"):Fragment {//newInstance(name: String):
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESC, description)
            val fragment = VotingRecordFragment()
            fragment.arguments = args
            return fragment
        }
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
            R.id.search_next -> {
                view!!.findViewById<ListView>(R.id.legis_votes).adapter.notifyDataSetChanged()
                Log.d("VOTES", "next")
            }
            R.id.search_back -> {
                Log.d("VOTES", "back")
            }
        }
    }

    //p0: List    p1: List item clicked    p2: Index of clicked item
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // List Item index is p2
        //val exText = p1!!.findViewById<TextView>(R.id.articleHeadline).text.toString()
        //val fragment = BillFragment.newInstance("H.R.1234", "115", "Robert Dunder", "[D-PA]", "Free Paper of Pensylvania Act")
       // fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()
        //p1!!.findViewById<View>(p2).findViewById<TextView>(R.id.articleHeadline).text.toString()
        Log.d("VOTES", "clocked %d".format(p2))//"Vote Clicker %d".format(p2))
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
        val rootView = inflater!!.inflate(R.layout.fragment_voting_record, container, false)
        rootView.findViewById<TextView>(R.id.legislatorName).text = arguments[ARG_NAME] as String
        rootView.findViewById<TextView>(R.id.legislatorDescription).text = arguments[ARG_DESC] as String
        rootView.findViewById<ListView>(R.id.legis_votes).onItemClickListener = this//setOnItemClickListener(this)
        //votes_list.adapter =
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
        doAsync {
            val name = arguments[ARG_NAME] as String
            val fname = (name).split(' ')[0]
            val lname = (name).split(' ').slice(IntRange(1,name.split(' ').size-2)).reduce{fn, next -> fn+" "+next}
            Log.d("VOTES",fname)
            Log.d("VOTES", lname)
            voteAdapter.getVotes(fname,lname)
            uiThread {
                Log.d("VOTES", "In UI Thread")
                votesList.adapter = voteAdapter
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
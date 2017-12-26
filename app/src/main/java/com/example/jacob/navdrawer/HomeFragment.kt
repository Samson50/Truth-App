package com.example.jacob.navdrawer

import android.support.v4.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Jacob on 12/4/2017.
 */
class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val TAG = "Fragment 2"
    lateinit var option: Spinner
    private val states = arrayOf("AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS",
            "KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR",
            "PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY")
    private val fullStates = arrayOf("Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware",
            "Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
            "New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina",
            "South Dakota","Tennessee","Texas","Utah","Vermont","Washington","West Virginia","Wisconsin","Wyoming")
    private lateinit var drawer: NavDrawerActivity
    //var cont: Context? = null

    companion object {
        //private val ARG_CAUGHT = "asdfadsf"
        fun newInstance():Fragment {//newInstance(caught: Pokemon):
            //val args: Bundle = Bundle()
            //args.putSerializeable(ARG_CAUGHT, caught)
            //val fragment = FirstFragment()
            //fragment.arguments = args
            //return fragment
            return HomeFragment()
        }
    }

    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
        drawer = context as NavDrawerActivity

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(TAG,"Spinner not Selected")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //states.get(p2)
        drawer.setState(states[p2])
        Log.d(TAG,"Spinner Selected")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.fragment_home, container, false)
        option = rootView.findViewById(R.id.state_select)
        option.adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,fullStates)
        option.onItemSelectedListener = this
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        val adapter = NewsFeedsAdapter(activity)
        val newsView = view!!.findViewById<ListView>(R.id.home_news)
        doAsync {
            adapter.getArticles("U.S.%20Congress")
            uiThread {
                newsView.adapter = adapter
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
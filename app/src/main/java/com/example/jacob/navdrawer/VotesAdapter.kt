package com.example.jacob.navdrawer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.jacob.navdrawer.R
import com.example.jacob.navdrawer.R.id.sponsor
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class VotesAdapter(context: Context): BaseAdapter() {

    private val mContext: Context = context
    private var choices: MutableList<String> = mutableListOf()
    private var names: MutableList<String> = mutableListOf()
    private var congresses: MutableList<String> = mutableListOf()
    private var questions: MutableList<String> = mutableListOf()

    private var numVotes: Int = 0
    private var page = 0
    private var perPage = 20
    private var firstName = ""
    private var lastName = ""
    private var search = ""
    private var searchParameters = ""
    //private var images: MutableList<ImageView> = mutableListOf()

    init {
        //mContext = context
    }

    fun setParams(searchString: String, parameters:String = ""){
        if (parameters!=""){
            searchParameters = parameters
        }
        if (searchString != "") {
            search = "$searchParameters&sname=$searchString"
        }
        else {
            search = searchParameters
        }
    }

    fun updatePage(p:Int,perp:Int){
        page = p
        perPage = perp
        getVotes(firstName,lastName)
    }

    private fun parse(json: String): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }

    fun getVotes(fname:String = firstName, lname: String = lastName) {
        firstName = fname
        lastName = lname
        val url = "http://192.168.1.72/api/legislator/getVotes.php?fname=%27"+fname+"%27&lname=%27"+lname.replace(" ","%20")+"%27&p=$page&perp=$perPage$search"
        Log.d("VOTES", url)
        val result = URL(url).readText()
        val json = parse(result)!!.getJSONArray("value")
        numVotes = json.length()
        Log.d("VOTES","votes %d".format(numVotes))
        // choice, name, congress, question
        for (i in 0 until json.length()) {
            Log.d("VOTES","loop %d".format(i))
            val article = json!!.getJSONObject(i)
            choices.add(i,article.getString("choice"))
            names.add(i,article.getString("name"))
            congresses.add(i,article.getString("congress"))
            questions.add(i,article.getString("question"))
        }
        Log.d("BILLS","Out of the thing")
    }

    // Render Each Row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val vote = layoutInflater.inflate(R.layout.info_vote, viewGroup, false)

        vote.findViewById<TextView>(R.id.vote_congress).text = congresses[position]
        vote.findViewById<TextView>(R.id.vote_bill).text = names[position]
        val voteCast = vote.findViewById<TextView>(R.id.vote_cast)
        if (choices[position] == "Yea" || choices[position] == "Aye") voteCast.setBackgroundColor(mContext.resources.getColor(R.color.yes))
        else if (choices[position] == "Nay" || choices[position] == "No") voteCast.setBackgroundColor(mContext.resources.getColor(R.color.no))
        else if (choices[position] == "Not") voteCast.setBackgroundColor(mContext.resources.getColor(R.color.colorBar))
        voteCast.text = choices[position]
        vote.findViewById<TextView>(R.id.vote_question).text = questions[position]
        return vote
    }

    override fun getItem(p0: Int): Any {
        return "TEST STRING"
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    // Responsible for rows in list
    override fun getCount(): Int {
        // Number of rows
        return numVotes
    }
}
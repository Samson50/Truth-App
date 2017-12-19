package com.example.jacob.navdrawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.jacob.navdrawer.R
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class NewsFeedsAdapter(context: Context): BaseAdapter() {

    private val mContext: Context = context
    private var searchData: JSONObject? = null
    private var titles: MutableList<String> = mutableListOf()
    private var descrs: MutableList<String> = mutableListOf()
    private var links: MutableList<String> = mutableListOf()
    private var images: MutableList<Bitmap> = mutableListOf()

    init {
        //mContext = context
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

    fun getArticles(name: String) {
        val nname = name.substring(0,name.length-7).replace(" ","%20",true)
        Log.d("News",nname)
        val url = "https://newsapi.org/v2/everything?q=$nname"+
                "&sortBy=popularity"+
                "&apiKey=ec291fab473f4d4c92df39bdfbebc903"
        val result = URL(url).readText()
        val json = parse(result)
        //var holder: ImageView? = null
        searchData = json
        for (i in 0..5) {
            val article = json!!.getJSONArray("articles").getJSONObject(i)
            val title = article.getString("title")
            titles.add(i,title)
            val descr = article.getString("description")
            descrs.add(i,descr)
            val link  = article.getString("url")
            links.add(i,link)
            val img = article.getString("urlToImage")
            val bmp = BitmapFactory.decodeStream(URL(img).openConnection().getInputStream())
            images.add(i,bmp)
        }
    }

    // Render Each Row
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val article = layoutInflater.inflate(R.layout.news_article, viewGroup, false)

        article.findViewById<TextView>(R.id.articleHeadline).text = titles[position]
        article.findViewById<TextView>(R.id.descriptionText).text = descrs[position]
        article.findViewById<TextView>(R.id.hyperLink).text = links[position]
        article.findViewById<ImageView>(R.id.articleImage).setImageBitmap(images[position])
        return article
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
        return 6
    }
}
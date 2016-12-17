package com.challenge.articlereaderkotlin

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Arthur on 16/12/2016.
 */
class ListFragment() : Fragment() {

    lateinit private var mArticleList:ArrayList<Article>

    fun newInstance(articles: ArrayList<Article>): ListFragment{
        this.mArticleList=articles
        return this
    }

    fun setArticleList(articles:ArrayList<Article>){
        mArticleList=articles
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.article_list, container, false)
//        val activity = activity
        val recyclerView =view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ArticleAdapter(activity,mArticleList)
        return recyclerView
    }
}
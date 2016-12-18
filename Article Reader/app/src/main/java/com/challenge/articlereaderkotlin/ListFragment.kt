package com.challenge.articlereaderkotlin


import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import java.util.*

/**
 * Created by Arthur on 16/12/2016.
 */
class ListFragment() : Fragment() {

    lateinit private var mArticleList:ArrayList<Article>

    fun newInstance(articles: ArrayList<Article>): ListFragment{
        this.setArticleList(articles)
        return this
    }

    fun setArticleList(articles:ArrayList<Article>){
        mArticleList=articles
    }

    fun sortList(method:Int){
        when(method){
            1 -> mArticleList.sortBy { article -> article.getDate() }
            2 -> mArticleList.sortBy { article -> article.getTitle() }
            3 -> mArticleList.sortBy { article -> article.getAuthors()}
            4 -> mArticleList.sortBy { article -> article.getWebsite()}
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_main,menu)
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
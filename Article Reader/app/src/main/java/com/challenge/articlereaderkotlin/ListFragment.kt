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

    /*Creates an internal instance of the Article list to be manipulated with sorting methods and
    * other operations, commiting changes to the dataBase only when needed*/
    lateinit private var mArticleList:ArrayList<Article>

    /*NewInstance method to serve as the constructor of the class*/
    fun newInstance(articles: ArrayList<Article>): ListFragment{
        this.setArticleList(articles)
        return this
    }

    /*function used to initialize or change the internal article List*/
    fun setArticleList(articles:ArrayList<Article>){
        mArticleList=articles
    }

    /*Sorting methods using Kotlin sortBy operator with articles*/
    fun sortList(method:Int){
        when(method){
            1 -> mArticleList.sortBy { article -> article.getDate() }
            2 -> mArticleList.sortBy { article -> article.getTitle() }
            3 -> mArticleList.sortBy { article -> article.getAuthors()}
            4 -> mArticleList.sortBy { article -> article.getWebsite()}
        }
    }

    /*Defines that the fragment has a custom options menu to set the "Sort By" menu in the
    * tool bar*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /*Clears the menu in order to prevent multiple instances of the same menu to be inflated*/
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_main,menu)
    }


    /*Creates and initializes a recyclerview that uses the ArticleAdapter class*/
    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.article_list, container, false)
        val recyclerView =view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ArticleAdapter(activity,mArticleList)
        return recyclerView
    }
}
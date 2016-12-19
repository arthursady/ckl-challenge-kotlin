package com.challenge.articlereaderkotlin

/**
 * Created by Arthur on 17/12/2016.
 */
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_element.view.*
import java.text.SimpleDateFormat
import java.util.*


class ArticleAdapter() : RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){

    /*Declaring internal variables to manipulate the adapter*/
    private val Read = 1        //Defining constants to use as state identifiers
    private val Unread = 0

    lateinit var mLayoutInflater: LayoutInflater

    /*Creating an instance of the interface to be implemented in the main activity in order to
    * define the actions of Clicking and LongClicking a view item*/
    lateinit var mListener : Interface

    /**/
    lateinit var mContext:Context
    lateinit private var mArticleList: ArrayList<Article>

    /*Custom constructor defined outside the main constructor which must be empty*/
    constructor(context: Context, list: ArrayList<Article>):this(){
        mContext=context
        mArticleList=list
        mLayoutInflater= LayoutInflater.from(mContext)
        mListener = mContext as Interface

    }

    /*Viewholder definition using kotlinx library to bind the views*/
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        /*Set the data inside the views of the list fragment*/
        fun setData(article:Article){
            itemView.title.text= mContext.getString(R.string.title_tag) + article.getTitle()
            itemView.authors.text=mContext.getString(R.string.authors_tag) + article.getAuthors()
            itemView.website.text=mContext.getString(R.string.website_tag) + article.getWebsite()

            /*formats the Date contained in the article to show only the necessary fields*/
            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            itemView.date.text=mContext.getString(R.string.date_tag) + dateFormat.
                    format(article.getDate())

            /*If the image is not provided in the article, sets a standard placeholder*/
            if((article.getImage()!=null) && !(article.getImage().equals("null"))){
                val id = mContext.resources.getIdentifier(article.getImage(),
                        "drawable",mContext.getPackageName())
                itemView.list_image.setImageResource(id)
            }
            else{
                val id = mContext.resources.getIdentifier("placeholder",
                        "drawable",mContext.getPackageName())
                itemView.list_image.setImageResource(id)
            }
        }
    }

    /*Inflates the viewHolder based on the viewtype which is defined based on the article
    * readState having different layouts for Read and Unread articles*/
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var itemView : View = mLayoutInflater.inflate(R.layout.list_element,parent,false)

        when (viewType){
            Read -> itemView = mLayoutInflater.inflate(R.layout.list_element_read,parent,false)
            Unread -> itemView = mLayoutInflater.inflate(R.layout.list_element,parent,false)
        }

        return ViewHolder(itemView)
    }

    /*Creates the listeners for Clicks and LongClicks that will be bind to the elements on the
    * Displayed list*/
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setData(mArticleList[position])

        holder?.itemView?.setOnClickListener {
            mListener.onArticleClicked(mArticleList[holder.adapterPosition])
        }

        holder?.itemView?.setOnLongClickListener{ v ->
            mListener.onArticleSelected(mArticleList[holder.adapterPosition], v, mContext)
            true
        }

    }

    /*Gets the view type based on the ReadState of the Articles*/
    override fun getItemViewType(position: Int): Int {
        if(mArticleList[position].getReadState()){
            return Read
        }
        else{
            return Unread
        }
    }

    /*Gets the number of elements in the list*/
    override fun getItemCount(): Int {
        return mArticleList.size
    }

    /*Defines the functions that will be present in the interface between the Fragment and the
    * activity*/
    interface Interface {
        fun onArticleClicked(article: Article)
        fun onArticleSelected(article: Article,view: View,context: Context)
    }
}
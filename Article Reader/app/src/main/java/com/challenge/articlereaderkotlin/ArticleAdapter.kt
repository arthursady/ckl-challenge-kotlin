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



    private val Read = 1
    private val Unread = 0
    lateinit var mLayoutInflater: LayoutInflater
    lateinit var mListener : Interface
    lateinit var mContext:Context
    lateinit private var mArticleList: ArrayList<Article>

    constructor(context: Context, list: ArrayList<Article>):this(){
        mContext=context
        mArticleList=list
        mLayoutInflater= LayoutInflater.from(mContext)
        mListener = mContext as Interface

    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        fun setData(article:Article){
            itemView.title.text= mContext.getString(R.string.title_tag) + article.getTitle()
            itemView.authors.text=mContext.getString(R.string.authors_tag) + article.getAuthors()
            itemView.website.text=mContext.getString(R.string.website_tag) + article.getWebsite()

            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            itemView.date.text=mContext.getString(R.string.date_tag) + dateFormat.
                    format(article.getDate())

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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var itemView : View = mLayoutInflater.inflate(R.layout.list_element,parent,false)

        when (viewType){
            Read -> itemView = mLayoutInflater.inflate(R.layout.list_element_read,parent,false)
            Unread -> itemView = mLayoutInflater.inflate(R.layout.list_element,parent,false)
        }

        return ViewHolder(itemView)
    }

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

    override fun getItemViewType(position: Int): Int {
        if(mArticleList[position].getReadState()){
            return Read
        }
        else{
            return Unread
        }
    }

    override fun getItemCount(): Int {
        return mArticleList.size
    }

    interface Interface {
        fun onArticleClicked(article: Article)
        fun onArticleSelected(article: Article,view: View,context: Context)
    }
}
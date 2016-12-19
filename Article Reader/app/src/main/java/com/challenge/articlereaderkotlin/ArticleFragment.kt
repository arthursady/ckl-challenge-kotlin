package com.challenge.articlereaderkotlin


import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.article_details.view.*
import java.text.SimpleDateFormat

/**
 * Created by Arthur on 16/12/2016.
 */
open class ArticleFragment() : Fragment(){

    /*Create article container to be initialized with the article information of the article to
    * be displayed*/
    lateinit private var mArticle: Article

    /*NewInstance method to serve as custom constructor*/
    fun newInstance(article: Article): ArticleFragment{
        this.mArticle=article
        return this
    }

    /*Creates the view and binds its values with the kotlinx library*/
    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        var view:View = inflater!!.inflate(R.layout.article_details,container,false)

        /*if the image is not present in the article shows the standard placeholder*/
        if ((mArticle.getImage()!= null)&& !(mArticle.getImage().equals("null"))){

            val id = context.resources.getIdentifier(mArticle.getImage(),
                    "drawable",context.getPackageName())
            view.image_details.setImageResource(id)
        }
        else{
            val id = context.resources.getIdentifier("placeholder",
                    "drawable",context.getPackageName())
            view.image_details.setImageResource(id)

        }

        /*in ordet to print the Tags, contructs a string with all the tags labels preceded by a
        * "#"*/
        var tags:String?=""
        for(i in mArticle.getTags()){
            tags=tags +"#"+i.toString()+" "
        }

        /*imput the calues in the corresponding text and image views*/
        view.title_details.text=mArticle.getTitle()
        view.tags_detail.text=tags
        view.authors_detail.text=mArticle.getAuthors()
        view.website_detail.text=mArticle.getWebsite()
        view.content.text=mArticle.getContent()

        /*Format the data to show only the necessary variables*/
        var dateFormat: SimpleDateFormat= SimpleDateFormat("MM/dd/yyyy")
        view.date_detail.text=dateFormat.format(mArticle.getDate())
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
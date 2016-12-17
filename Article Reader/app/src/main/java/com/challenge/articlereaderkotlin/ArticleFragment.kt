package com.challenge.articlereaderkotlin

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.Unbinder
import java.text.SimpleDateFormat

/**
 * Created by Arthur on 16/12/2016.
 */
open class ArticleFragment() : Fragment(){

    lateinit private var mArticle: Article
    lateinit private var unbinder: Unbinder

    fun newInstance(article: Article): ArticleFragment{
        this.mArticle=article
        return this
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return super.onCreateView(inflater, container, savedInstanceState)

        var view:View=inflater!!.inflate(R.layout.article_details,container,false)

        unbinder=ButterKnife.bind(this,view)

        var titleTextView: TextView = ButterKnife.findById(view,R.id.title_details)
        var tagsTextView: TextView = ButterKnife.findById(view,R.id.tags_detail);
        var imageView: ImageView = ButterKnife.findById(view,R.id.image_details);
        var authorsTextView: TextView = ButterKnife.findById(view,R.id.authors_detail);
        var websiteTextView: TextView = ButterKnife.findById(view,R.id.website_detail);
        var contentTextView: TextView = ButterKnife.findById(view,R.id.content);
        var dateTextView: TextView = ButterKnife.findById(view,R.id.date_detail);

        if ((mArticle.getImage()!=null)&& !(mArticle.getImage().equals("null"))){

            imageView.setImageResource(getResources().getIdentifier(mArticle.getImage(),"drawable",
                    getActivity().getPackageName()))
        }
        else{
            imageView.setImageResource(getResources().getIdentifier("placeholder","drawable",
                    getActivity().getPackageName()))
        }

        var tags:String?=""
        for(i in mArticle.getTags()){
            tags=tags +"#"+i.toString()+" "
        }

        tagsTextView.setText(tags)
        titleTextView.setText(mArticle.getTitle())
        authorsTextView.setText(mArticle.getAuthors())
        websiteTextView.setText(mArticle.getWebsite())
        contentTextView.setText(mArticle.getContent())

        var dateFormat: SimpleDateFormat= SimpleDateFormat("MM/dd/yyyy")
        dateTextView.setText(dateFormat.format(mArticle.getDate()))

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}
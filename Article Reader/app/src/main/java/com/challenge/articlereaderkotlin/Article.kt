package com.challenge.articlereaderkotlin

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/**
 * Created by Arthur on 14/12/2016.
 */
@RealmClass
open class Article : RealmObject() {

    /*Defined variable to keep track of the article state either Read or Unread*/
    private var read: Boolean = false

    /*model variable definitions to match the imported model from the challenge URL*/
    @PrimaryKey
    lateinit private var title: String
    lateinit private var authors: String
    lateinit private var website:String
    lateinit private var date:Date
    lateinit private var content: String
    private var image: String?=null

    /*This model posses an internal model that is composed of a list of tags*/
    lateinit private var tags:RealmList<Tags>


    /*Setter for the ReadState manipulation*/
    open fun setRead(status: Boolean) {
        read = status
    }

    /*getters for the other internal variables*/
    open fun getTitle():String{
        return title
    }

    open fun getAuthors():String{
        return authors
    }

    open fun getWebsite():String{
        return website
    }

    open fun getDate():Date{
        return date
    }

    open fun getContent():String{
        return content
    }

    open fun getImage():String?{
        return image
    }

    open fun getTags():RealmList<Tags>{
        return tags
    }

    open fun getReadState():Boolean{
        return read
    }
}
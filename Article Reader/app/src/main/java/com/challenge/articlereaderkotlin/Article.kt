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

    private var read: Boolean = false

    @PrimaryKey
    lateinit private var title: String
    lateinit private var authors: String
    lateinit private var website:String
    lateinit private var date:Date
    lateinit private var content: String
    private var image: String?=null
    lateinit private var tags:RealmList<Tags>


    open fun setRead(status: Boolean) {
        read = status
    }

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
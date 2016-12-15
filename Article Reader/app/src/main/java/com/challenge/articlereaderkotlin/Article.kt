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
    private var title: String? = null
    private var authors: String? = null
    private var website:String?=null
    private var date:Date?=null
    private var content: String?=null
    private var image: String?=null
    private var tags:RealmList<Tags>? = null


    open fun getTitle():String?{
        return title
    }
}
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

    @PrimaryKey
    open var title: String? = null
    open var authors: String? = null
    open var website:String?=null
    open var date:Date?=null
    open var content: String?=null
    open var image: String?=null
    open var tags:RealmList<Tags>? = null
}
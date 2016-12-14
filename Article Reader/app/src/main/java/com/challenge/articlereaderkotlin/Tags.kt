package com.challenge.articlereaderkotlin

import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by Arthur on 14/12/2016.
 */
@RealmClass
open class Tags : RealmObject(){
    open var id: Int?=null
    open var label: String?=null
}
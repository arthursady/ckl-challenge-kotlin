package com.challenge.articlereaderkotlin

import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by Arthur on 14/12/2016.
 */
@RealmClass
open class Tags(private var id:Int,private var label: String) : RealmObject(){

    override fun toString(): String {
        return label
    }
}
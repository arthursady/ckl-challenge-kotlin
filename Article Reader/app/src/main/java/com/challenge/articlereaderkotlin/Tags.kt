package com.challenge.articlereaderkotlin

import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by Arthur on 14/12/2016.
 */
@RealmClass
open class Tags() : RealmObject(){

    private var id :Int?=null
    lateinit private var label: String

    override fun toString(): String {
        return label
    }
}
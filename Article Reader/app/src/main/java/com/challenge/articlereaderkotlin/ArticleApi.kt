package com.challenge.articlereaderkotlin

import retrofit2.Call
import retrofit2.http.GET
import java.util.*

/**
 * Created by Arthur on 14/12/2016.
 */
interface ArticleApi {
    @GET("/challenge")
    abstract fun retrieveArticles(): Call<ArrayList<Article>>
}
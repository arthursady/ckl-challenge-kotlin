package com.challenge.articlereaderkotlin

import java.util.*

/**
 * Created by Arthur on 16/12/2016.
 */
interface DownloadListener {
    fun downloadComplete(articles:ArrayList<Article> )
}
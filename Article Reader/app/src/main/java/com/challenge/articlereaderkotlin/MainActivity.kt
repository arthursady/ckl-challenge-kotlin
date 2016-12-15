package com.challenge.articlereaderkotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.GsonBuilder
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        if(isNetworkconnected()){
            var progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please wait...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            makeRetrofitCalls()
        }
        else{
           AlertDialog.Builder(this)
            .setTitle("No Connection!")
            .setMessage("You are not Connected to the Internet, please reconnect and try")
            .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener {
                dialogInterface, i ->  })
            .show()
        }

    }

    fun isNetworkconnected(): Boolean{
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun downloadComplete (articles :ArrayList<Article>){
        var realm = Realm.getDefaultInstance()
        var dbArticles = realm.where(Article::class.java).findAll()

        if (dbArticles.size > 0) run {
            for (i in articles.indices) {
                if (realm.where(Article::class.java).equalTo("title", articles[i]
                        .getTitle()).findFirst() == null) {
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(articles[i])
                    realm.commitTransaction()
                }
            }
        }
        else{
            for (i in articles.indices) {
                realm.beginTransaction()
                realm.copyToRealmOrUpdate(articles[i])
                realm.commitTransaction()
            }
        }
        realm.close()
    }

    fun makeRetrofitCalls(){
        val gson = GsonBuilder().setDateFormat("MM/dd/yyyy").create()

        val retrofit = Retrofit.Builder()
                .baseUrl ("https://www.ckl.io")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val ArticleAPI = retrofit.create < ArticleApi >(ArticleApi::class.java)

        val call = ArticleAPI.retrieveArticles()

        call.enqueue(object : Callback<ArrayList<Article>> {
            override fun onResponse(call: Call<ArrayList<Article>>,
                                    response: Response<ArrayList<Article>>) {
                downloadComplete(response.body())
            }

            override fun onFailure(call: Call<ArrayList<Article>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
            }
        })

    }



}

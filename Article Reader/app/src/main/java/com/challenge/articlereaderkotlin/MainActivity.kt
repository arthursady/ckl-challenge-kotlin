package com.challenge.articlereaderkotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.PopupMenu
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(),DownloadListener,ArticleAdapter.Interface {

    val mSortDate = 1
    val mSortTitle = 2
    val mSortAuthor = 3
    val mSortWebsite = 4

    lateinit var mListFragment : ListFragment
    lateinit var mRealm : Realm
    var mProgressDialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        mRealm=Realm.getDefaultInstance()

        if(isNetworkconnected()){
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage("Please wait...")
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.show()

            makeRetrofitCalls()
        }
        else{
           AlertDialog.Builder(this)
            .setTitle("No Connection!")
            .setMessage("You are not Connected to the Internet, couldn't load new articles")
            .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener {
                dialogInterface, i ->  })
            .show()


            var dbArticles = mRealm.where(Article::class.java).findAll()

            if (dbArticles.size > 0) run {
                var dbList = ArrayList<Article>()
                dbList.addAll(mRealm.where(Article::class.java).findAll().
                        subList(0,dbArticles.size))
                showListFragment(dbList)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    fun getScreenSize():Double{
        var dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)
        val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
        val screenInches = Math.sqrt(x+y)
        return screenInches
    }

    fun isNetworkconnected(): Boolean{
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun downloadComplete (articles :ArrayList<Article>){

        var dbArticles = mRealm.where(Article::class.java).findAll()

        if (dbArticles.size > 0) run {
            for (i in articles.indices) {
                if (mRealm.where(Article::class.java).equalTo("title", articles[i]
                        .getTitle()).findFirst() == null) {
                    mRealm.beginTransaction()
                    mRealm.copyToRealmOrUpdate(articles[i])
                    mRealm.commitTransaction()
                }
            }
        }
        else{
            for (i in articles.indices) {
                mRealm.beginTransaction()
                mRealm.copyToRealmOrUpdate(articles[i])
                mRealm.commitTransaction()
            }
        }
        var realmResults = mRealm.where(Article::class.java).findAll()
        var dbList = ArrayList<Article>()

        dbList.addAll(mRealm.where(Article::class.java).findAll().subList(0,realmResults.size))

        showListFragment(dbList)

        mProgressDialog?.hide()



    }

    fun showListFragment(articles: ArrayList<Article>){
        mListFragment = ListFragment().newInstance(articles)

        if(getScreenSize()>7){
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.list_container_largescreens,mListFragment,"articleList")
                    .commit()
        }
        else {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root_layout, mListFragment, "articleList")
                    .commit()
        }

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.byDate -> {
                mListFragment.sortList(mSortDate)
                supportFragmentManager.beginTransaction().detach(mListFragment).commit()
                supportFragmentManager.beginTransaction().attach(mListFragment).commit()
            }

            R.id.byTitle ->{
                mListFragment.sortList(mSortTitle)
                supportFragmentManager.beginTransaction().detach(mListFragment).commit()
                supportFragmentManager.beginTransaction().attach(mListFragment).commit()
            }

            R.id.byAuthor ->{
                mListFragment.sortList(mSortAuthor)
                supportFragmentManager.beginTransaction().detach(mListFragment).commit()
                supportFragmentManager.beginTransaction().attach(mListFragment).commit()
            }

            R.id.byWebsite ->{
                mListFragment.sortList(mSortWebsite)
                supportFragmentManager.beginTransaction().detach(mListFragment).commit()
                supportFragmentManager.beginTransaction().attach(mListFragment).commit()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onArticleClicked(article: Article) {
        mRealm.beginTransaction()
        article.setRead(true)
        mRealm.commitTransaction()

        val detailsFragment = ArticleFragment().newInstance(article)

        if(getScreenSize()<7){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.root_layout,detailsFragment,"articleDetails")
                    .addToBackStack(null)
                    .commit()
        }
        else{
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.root_layout,detailsFragment,"articleDetails")
                    .commit()

            supportFragmentManager.beginTransaction().detach(mListFragment).commit()
            supportFragmentManager.beginTransaction().attach(mListFragment).commit()
        }
    }

    override fun onArticleSelected(article: Article, view: View, context: Context) {
        var popup = PopupMenu(context, view)
        if(article.getReadState()){
            popup.menuInflater.inflate(R.menu.popup_menu_read,popup.menu)
        }
        else{
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
        }

        popup.show()

        popup.setOnMenuItemClickListener {
            Toast.makeText(context, "You clicked: "+ title, Toast.LENGTH_SHORT )

            if(article.getReadState()){
                mRealm.beginTransaction()
                article.setRead(false)
                mRealm.commitTransaction()
            }
            else{
                mRealm.beginTransaction()
                article.setRead(true)
                mRealm.commitTransaction()
            }

            supportFragmentManager.beginTransaction().detach(mListFragment).commit()
            supportFragmentManager.beginTransaction().attach(mListFragment).commit()

            true
        }
    }


}

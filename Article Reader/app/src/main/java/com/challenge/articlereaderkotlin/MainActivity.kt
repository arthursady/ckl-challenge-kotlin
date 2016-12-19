package com.challenge.articlereaderkotlin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
    /*Define the constant values used in the sorting methods selection*/
    val mSortDate = 1
    val mSortTitle = 2
    val mSortAuthor = 3
    val mSortWebsite = 4

    /*Creates an instance of the ListFragment that is used in more than one place in the code
    * and also defines an instance of the DB*/
    lateinit var mListFragment : ListFragment
    lateinit var mRealm : Realm

    /*Creates a progress dialog to be shown while downloading the data from the URL*/
    var mProgressDialog : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*Initializes the DB and sets it with the default configuration*/
        Realm.init(this)
        mRealm=Realm.getDefaultInstance()

        /*Checks for connection, if there is connection sets the progressDialog and starts the
        * download for the articles*/
        if(isNetworkconnected()){
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.setMessage("Please wait...")
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.show()

            makeRetrofitCalls()
        }
            /*if there is no connection shows an AlertDialog and an if there are articles in the
            * databese display them*/
        else{
           AlertDialog.Builder(this)
            .setTitle("No Connection!")
            .setMessage("You are not Connected to the Internet, couldn't load new articles")
            .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener {
                dialogInterface, i ->  })
            .show()

            /*If dbArticles is not empty displays the already downloaded articles*/
            var dbArticles = mRealm.where(Article::class.java).findAll()

            if (dbArticles.size > 0) run {
                var dbList = ArrayList<Article>()
                dbList.addAll(mRealm.where(Article::class.java).findAll().
                        subList(0,dbArticles.size))
                showListFragment(dbList)
            }
        }

    }
    /*when destroying the activity, closes the instance of the DB*/
    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    /*Calculates the screen size in inches in order to provide a usable parameter to define when to
    * use the X-Large layout or the normal layout*/
    fun getScreenSize():Double{
        var dm = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(dm)
        val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
        val screenInches = Math.sqrt(x+y)
        return screenInches
    }

    /*Determines if there is connection*/
    fun isNetworkconnected(): Boolean{
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /*Imput the new items in the DB and calls for the function to display the list of fragments*/
    override fun downloadComplete (articles :ArrayList<Article>){

        var dbArticles = mRealm.where(Article::class.java).findAll()

        /*If the DB is not empty then checks every article primary key and only add the ones that
        * are different*/
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

        /*Once the inputs are in the DB, creates a copy arraylist to pass to the fragment List
        * to simplify the usage of a normal recycler view*/
        val realmResults = mRealm.where(Article::class.java).findAll()
        val dbList = ArrayList<Article>()

        dbList.addAll(mRealm.where(Article::class.java).findAll().subList(0,realmResults.size))

        showListFragment(dbList)

        /*Once the article list is passed to the ListFragment get the */
        mProgressDialog?.hide()
    }

    /*Displays the listFragment according to the screen size*/
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

    /*Invoke the API*/
    fun makeRetrofitCalls(){
        /*creates a dateFormat to convert correctly from the URL*/
        val gson = GsonBuilder().setDateFormat("MM/dd/yyyy").create()
        /*creates the Retrofit instance with the challenge URL*/
        val retrofit = Retrofit.Builder()
                .baseUrl ("https://www.ckl.io")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        /*Instantiates the ArticleApi interface */
        val ArticleAPI = retrofit.create < ArticleApi >(ArticleApi::class.java)

        val call = ArticleAPI.retrieveArticles()

        /*enqueues the callback and sets the actions for the on response and on failure*/
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

    /*Sets the listeners for the Sort By menu*/
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            /*Sorts the list based on the selected method and refreshs the fragment view*/
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

    /*Implements the onClick method of the ArticleAdapter interface*/
    override fun onArticleClicked(article: Article) {
        /*Changes the read state and commit to the DB*/
        mRealm.beginTransaction()
        article.setRead(true)
        mRealm.commitTransaction()

        /*Instantiates a details fragment*/
        val detailsFragment = ArticleFragment().newInstance(article)

        /*Sets the fragment in the correct layout depending on the screen size*/
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

    /*Implements the Long click listener from the ArticleAdapter interface*/
    override fun onArticleSelected(article: Article, view: View, context: Context) {
        /*Creates a popup menu to show the mark as Read/Unread option for the selected view*/
        var popup = PopupMenu(context, view)
        if(article.getReadState()){
            popup.menuInflater.inflate(R.menu.popup_menu_read,popup.menu)
        }
        else{
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
        }

        popup.show()

        /*If the article is Read, marks it as unread and if it is Unread marks as Read*/
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

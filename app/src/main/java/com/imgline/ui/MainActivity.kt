package com.imgline.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.imgline.R
import com.imgline.data.FeedViewModel
import com.imgline.data.database.EntityFeed

class MainActivity : AppCompatActivity() {
    val feedsViewModel : FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        val navView = findViewById<RecyclerView>(R.id.recycler_navigation_view)
        configureRecyclerView(navView, drawerLayout)

    }

    fun configureRecyclerView(recyclerView: RecyclerView, drawerLayout: DrawerLayout?) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val navController = findNavController(R.id.android_nav_frag)
        val addPostClickListener = View.OnClickListener {
            navController.navigate(R.id.create_feed)
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        val adapter = FeedAdapter(listOf(), addPostClickListener)
        recyclerView.adapter = adapter
        feedsViewModel.getFeeds().observe(this, Observer<List<EntityFeed>>{
            newList  ->
                adapter.feeds = newList
                adapter.notifyDataSetChanged()
            }
        )
    }
}

class FeedItem(itemView : View, clickListener: View.OnClickListener) : RecyclerView.ViewHolder(itemView){

    private lateinit var mFeed : EntityFeed

    init {
        itemView.setOnClickListener(clickListener)
    }

    fun bind(feed: EntityFeed) {
        mFeed = feed
    }

}

class FeedAdapter(var feeds :List<EntityFeed>,
                  val addPost: View.OnClickListener
                  ): RecyclerView.Adapter<FeedItem>() {

    companion object {
        val ADD_MENU_ITEM = 1
        val FEED_MENU_ITEM = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItem {
     return  FeedItem(
            LayoutInflater.from(parent.context).inflate(
                R.layout.navigation_view_item,
                parent,
                false
            ), addPost
        )
    }



    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ADD_MENU_ITEM
            else -> FEED_MENU_ITEM
         }


    override fun getItemCount(): Int {
        return feeds.size + 1
    }

    override fun onBindViewHolder(holder: FeedItem, position: Int) {
        if (position > 0) {
            holder.bind(feeds[position - 1])
        }
    }

}

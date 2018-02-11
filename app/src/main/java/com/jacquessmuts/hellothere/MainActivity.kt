package com.jacquessmuts.hellothere

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jacquessmuts.hellothere.data.HelloThereItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var exoPlayers: ArrayList<SimpleExoPlayer>

    private val maxPlayers = 10
    private var mCurrentlySelectedExoPlayer = 0 //loops from 0-maxPlayers

    private val random = Random()
    private var mHelloThereItems : ArrayList<HelloThereItem> = ArrayList()
    private lateinit var mAdapter : HelloThereAdapter

//    private lateinit var mShareActionProvider : ShareActionProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupExoPlayer()
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.menu_item_share -> {
                shareApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView(){
        //adding a layoutmanager
        recycler_view.layoutManager = GridLayoutManager(this, 4)

        recycler_view.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val gridLayoutManager : GridLayoutManager = recyclerView!!.layoutManager as GridLayoutManager
                if (gridLayoutManager.findLastVisibleItemPosition() > mHelloThereItems.size - 10){
                    addMoreKenobi()
                }
                sayHelloThere(HelloThereItem(0))
            }
        })

        addMoreKenobi()
    }

    /**
     * Add 1000 kenobi items
     */
    private fun addMoreKenobi(){
        var iterator = mHelloThereItems.size
        val initialSize = mHelloThereItems.size
        while (iterator < initialSize+1000){
            val helloThereItem = HelloThereItem(iterator)
            if (rand(0, 1001) > 999){
                helloThereItem.isGrievious = true
            }
            mHelloThereItems.add(helloThereItem)
            iterator++
        }

        mAdapter = HelloThereAdapter(mHelloThereItems, {sayHelloThere(it)})
        recycler_view.adapter = mAdapter
    }

    private fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    private fun setupExoPlayer() {
        val trackSelector = DefaultTrackSelector()

        this.exoPlayers = ArrayList()
        while (mCurrentlySelectedExoPlayer < maxPlayers) {
            exoPlayers.add(ExoPlayerFactory.newSimpleInstance(baseContext, trackSelector))
            exoPlayers[mCurrentlySelectedExoPlayer].playWhenReady = true
            mCurrentlySelectedExoPlayer++
        }
        mCurrentlySelectedExoPlayer = 0
    }


    private fun sayHelloThere(item : HelloThereItem){

        var uriString = "asset:///hello_there.mp3"

        if (item.isGrievious){
            uriString = "asset:///general_kenobi.mp3"
        }
        val mediaUri = Uri.parse(uriString)

        val userAgent = Util.getUserAgent(baseContext, "ExoPlayer")
        val mediaSource = ExtractorMediaSource(mediaUri,
                DefaultDataSourceFactory(baseContext, userAgent),
                DefaultExtractorsFactory(), null, null)

        val exoPlayer = exoPlayers[mCurrentlySelectedExoPlayer]

        if (!exoPlayer.isLoading &&
                (exoPlayer.playbackState == SimpleExoPlayer.STATE_IDLE ||
                        exoPlayer.playbackState == SimpleExoPlayer.STATE_ENDED) ) {
            exoPlayer.prepare(mediaSource)
        }

        iterateExoPlayer()
    }

    private fun iterateExoPlayer(){
        mCurrentlySelectedExoPlayer++
        if (mCurrentlySelectedExoPlayer > maxPlayers -1){
            mCurrentlySelectedExoPlayer = 0
        }
    }


    private fun shareApp(){
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = getString(R.string.share_text)
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(sharingIntent)
    }
    override fun onDestroy() {
        mCurrentlySelectedExoPlayer = 0

        while (mCurrentlySelectedExoPlayer < maxPlayers) {
            exoPlayers[mCurrentlySelectedExoPlayer].release()
            mCurrentlySelectedExoPlayer++
        }
        mCurrentlySelectedExoPlayer = 0
        exoPlayers = ArrayList()

        super.onDestroy()
    }
}






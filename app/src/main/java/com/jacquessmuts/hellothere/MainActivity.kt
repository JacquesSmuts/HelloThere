package com.jacquessmuts.hellothere

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jacquessmuts.hellothere.data.HelloThereItem


class MainActivity : AppCompatActivity() {

    private lateinit var exoPlayers: ArrayList<SimpleExoPlayer>

    private val MAX_PLAYERS = 10
    private var mCurrentlySelectedExoPlayer = 0 //loops from 0-MAX_PLAYERS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupExoPlayer()
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        //adding a layoutmanager
        recyclerView.layoutManager = GridLayoutManager(this, 4) as RecyclerView.LayoutManager?

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                sayHelloThere(HelloThereItem(0));
            }
        });

        //crating an arraylist to store users using the data class user
        val helloThereItems = ArrayList<HelloThereItem>()

        var iterator = 0
        while (iterator < 1000){
            helloThereItems.add(HelloThereItem(iterator))
            iterator++
        }

        //creating our adapter
        val adapter = HelloThereAdapter(helloThereItems, {sayHelloThere(it)})

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }

    private fun setupExoPlayer() {
        val trackSelector = DefaultTrackSelector()

        exoPlayers = ArrayList()
        while (mCurrentlySelectedExoPlayer < MAX_PLAYERS) {
            exoPlayers.add(ExoPlayerFactory.newSimpleInstance(baseContext, trackSelector))
            exoPlayers.get(mCurrentlySelectedExoPlayer).playWhenReady = true
            mCurrentlySelectedExoPlayer++
        }
        mCurrentlySelectedExoPlayer = 0
    }


    private fun sayHelloThere(item : HelloThereItem){

        val mediaUri = Uri.parse("asset:///hello_there.mp3")

        val userAgent = Util.getUserAgent(baseContext, "ExoPlayer")
        val mediaSource = ExtractorMediaSource(mediaUri,
                DefaultDataSourceFactory(baseContext, userAgent),
                DefaultExtractorsFactory(), null, null)

        val exoPlayer = exoPlayers.get(mCurrentlySelectedExoPlayer)

        if (!exoPlayer.isLoading &&
                (exoPlayer.playbackState == SimpleExoPlayer.STATE_IDLE ||
                        exoPlayer.playbackState == SimpleExoPlayer.STATE_ENDED) ) {
            exoPlayer.prepare(mediaSource)
        }

        iterateExoPlayer()
    }

    private fun iterateExoPlayer(){
        mCurrentlySelectedExoPlayer++
        if (mCurrentlySelectedExoPlayer > MAX_PLAYERS-1){
            mCurrentlySelectedExoPlayer = 0
        }
    }

    override fun onDestroy() {
        mCurrentlySelectedExoPlayer = 0

        while (mCurrentlySelectedExoPlayer < MAX_PLAYERS) {
            exoPlayers.get(mCurrentlySelectedExoPlayer).release()
            mCurrentlySelectedExoPlayer++
        }
        mCurrentlySelectedExoPlayer = 0
        exoPlayers = ArrayList()

        super.onDestroy()
    }
}






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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var exoPlayers: ArrayList<SimpleExoPlayer>

    private val MAX_PLAYERS = 12
    private var mCurrentlySelectedExoPlayer = 0 //loops from 0-MAX_PLAYERS

    val random = Random()
    private var mHelloThereItems : ArrayList<HelloThereItem> = ArrayList()
    private lateinit var mAdapter : HelloThereAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupExoPlayer()
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        //adding a layoutmanager
        recycler_view.layoutManager = GridLayoutManager(this, 4)

        recycler_view.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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

    fun rand(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    private fun setupExoPlayer() {
        val trackSelector = DefaultTrackSelector()

        this.exoPlayers = ArrayList()
        while (mCurrentlySelectedExoPlayer < MAX_PLAYERS) {
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
        if (mCurrentlySelectedExoPlayer > MAX_PLAYERS-1){
            mCurrentlySelectedExoPlayer = 0
        }
    }

    override fun onDestroy() {
        mCurrentlySelectedExoPlayer = 0

        while (mCurrentlySelectedExoPlayer < MAX_PLAYERS) {
            exoPlayers[mCurrentlySelectedExoPlayer].release()
            mCurrentlySelectedExoPlayer++
        }
        mCurrentlySelectedExoPlayer = 0
        exoPlayers = ArrayList()

        super.onDestroy()
    }
}






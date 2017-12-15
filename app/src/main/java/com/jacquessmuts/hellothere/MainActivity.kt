package com.jacquessmuts.hellothere

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.jacquessmuts.hellothere.data.HelloThereItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        //adding a layoutmanager
        recyclerView.layoutManager = GridLayoutManager(this, 4)


        //crating an arraylist to store users using the data class user
        val helloThereItems = ArrayList<HelloThereItem>()

        var iterator = 0;
        while (iterator < 1000){
            helloThereItems.add(HelloThereItem(iterator))
            iterator++
        }

        //creating our adapter
        val adapter = HelloThereAdapter(helloThereItems)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter
    }
}

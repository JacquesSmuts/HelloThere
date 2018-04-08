package com.jacquessmuts.hellothere

/**
* Created by Jacques Smuts on 12/15/2017.
*/

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacquessmuts.hellothere.data.HelloThereItem
import pl.droidsonroids.gif.GifImageView

class HelloThereAdapter(val helloThereList: ArrayList<HelloThereItem>,
                        val itemClick: (HelloThereItem) -> Unit) :
                        RecyclerView.Adapter<HelloThereAdapter.ViewHolder>() {

    companion object {
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelloThereAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_hello_there, parent, false)
        return ViewHolder(v, itemClick)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: HelloThereAdapter.ViewHolder, position: Int) {
        holder.bindItems(helloThereList[position])

        var imageResource = R.drawable.maingif
        if (helloThereList[position].isGrievious){
            imageResource = R.drawable.secondgif
        }

        holder.imageView.setImageResource(imageResource)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return helloThereList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View, private val itemClick: (HelloThereItem) -> Unit) : RecyclerView.ViewHolder(itemView) {

        var imageView : GifImageView = itemView.findViewById(R.id.image_hello_there)

        fun bindItems(item: HelloThereItem) {
//            val imageViewHelloThere = itemView.findViewById<GifImageView>(R.id.image_hello_there)
//            Picasso.with(itemView.context).load(HelloThereAdapter.HELLO_THERE_URL).into(imageViewHelloThere);
            itemView.setOnClickListener{ itemClick(item)}
        }
    }
}
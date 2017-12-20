package com.jacquessmuts.hellothere

/**
 * Created by smuts on 12/15/2017.
 */

import android.media.MediaPlayer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jacquessmuts.hellothere.data.HelloThereItem
import com.squareup.picasso.Picasso
import pl.droidsonroids.gif.GifImageView

/**
 * Created by Belal on 6/19/2017.
 */

class HelloThereAdapter(val helloThereList: ArrayList<HelloThereItem>,
                        val itemClick: (HelloThereItem) -> Unit) :
                        RecyclerView.Adapter<HelloThereAdapter.ViewHolder>() {

    companion object {
        const val HELLO_THERE_URL = "https://vignette.wikia.nocookie.net/dbz-dokkanbattle/images/d/d7/Hello_there.gif/revision/latest?cb=20170920015241";
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelloThereAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_hello_there, parent, false)
        return ViewHolder(v, itemClick)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: HelloThereAdapter.ViewHolder, position: Int) {
        holder.bindItems(helloThereList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return helloThereList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View, private val itemClick: (HelloThereItem) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: HelloThereItem) {
//            val imageViewHelloThere = itemView.findViewById<GifImageView>(R.id.image_hello_there)
//            Picasso.with(itemView.context).load(HelloThereAdapter.HELLO_THERE_URL).into(imageViewHelloThere);
            itemView.setOnClickListener{ itemClick(item)}
        }
    }
}
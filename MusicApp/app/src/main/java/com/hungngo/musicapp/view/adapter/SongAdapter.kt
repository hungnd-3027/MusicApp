package com.hungngo.musicapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hungngo.musicapp.R
import com.hungngo.musicapp.data.model.Song

class SongAdapter(var listSongs: MutableList<Song>, var context: Context?) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private lateinit var sendDataInterface: SendDataInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        sendDataInterface = context as SendDataInterface

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listSongs[position].name
        holder.tvSinger.text = listSongs[position].singer

        holder.itemView.setOnClickListener {
            sendDataInterface.sendDataToPlay(position)
            sendDataInterface.setListSongHeight()
        }
    }

    override fun getItemCount(): Int = listSongs.size

    interface SendDataInterface {
        fun sendDataToPlay(uri: Int)
        fun setListSongHeight()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSong: ImageView = itemView.findViewById(R.id.image_avatar)
        val tvName: TextView = itemView.findViewById<TextView>(R.id.text_song_name)
        val tvSinger: TextView = itemView.findViewById(R.id.text_singer)
    }
}

package com.hungngo.musicapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hungngo.musicapp.R
import com.hungngo.musicapp.model.Song
import kotlinx.android.synthetic.main.activity_main.*

class SongAdapter(var listSongs: MutableList<Song>, var context: Context?) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private lateinit var sendDataInterface: SendDataInterface

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSong = itemView.findViewById<ImageView>(R.id.img_avatar)
        val tvName = itemView.findViewById<TextView>(R.id.tv_song_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        sendDataInterface = context as SendDataInterface

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listSongs[position].toString()
        val activity = context as AppCompatActivity
        val initHeight = activity.frl_list_song.height

        holder.itemView.setOnClickListener {
            //send Data to MainActivity
            sendDataInterface.sendDataToPlay(position)

            // set height of ListSongsFragmnet
            val frlList = activity.frl_list_song  //get FrameLayout contains ListSongsFragment

            val frlController =
                activity.frl_controller_song  //get FrameLayout contains ControllerFragment
            //  newHeight = currentHeight of ListSongsFragment - currentHeight of ControllerFragment
            if (frlList.height > (initHeight - frlController.height)) {
                frlList.layoutParams.height = frlList.height - frlController.height
            }

        }
    }

    override fun getItemCount(): Int = listSongs.size

    interface SendDataInterface {
        fun sendDataToPlay(uri : Int)
    }
}
package com.hungngo.musicapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hungngo.musicapp.R
import com.hungngo.musicapp.data.model.Song
import com.hungngo.musicapp.view.adapter.SongAdapter
import java.io.Serializable


private const val BUNDLE_LIST_SONGS = "BUNDLE_LIST_SONGS"

class ListSongsFragment : Fragment() {
    private var listSongs: MutableList<Song>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listSongs = it.getSerializable(BUNDLE_LIST_SONGS) as MutableList<Song>?
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_songs, container, false)

        val rcvSongs = view.findViewById<RecyclerView>(R.id.rcv_list_song)
        if (listSongs != null) {
            val songAdapter = SongAdapter(listSongs!!, context)
            rcvSongs.adapter = songAdapter
            rcvSongs.layoutManager = LinearLayoutManager(context)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(listSongs: MutableList<Song>?) = ListSongsFragment().apply {
            arguments = Bundle().apply {
                putSerializable(BUNDLE_LIST_SONGS, listSongs as Serializable)
            }
        }
    }
}

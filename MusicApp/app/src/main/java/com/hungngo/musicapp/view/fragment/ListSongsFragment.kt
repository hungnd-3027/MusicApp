package com.hungngo.musicapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hungngo.musicapp.MyCommon
import com.hungngo.musicapp.R
import com.hungngo.musicapp.view.adapter.SongAdapter


class ListSongsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_songs, container, false)

        val rcvSongs = view.findViewById<RecyclerView>(R.id.rcv_list_song)
        val songAdapter = SongAdapter(MyCommon.listSongs, context)
        rcvSongs.adapter = songAdapter
        rcvSongs.layoutManager = LinearLayoutManager(context)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ListSongsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
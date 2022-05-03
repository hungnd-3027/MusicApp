package com.hungngo.musicapp.presenter

import com.hungngo.musicapp.data.model.Song

interface MainActivityContract {
    interface View {
        fun onGetListSongsSuccess(songs: MutableList<Song>)
        fun onGetListSongsFailed()
    }

    interface Presenter {
        fun getListSongs()
    }
}

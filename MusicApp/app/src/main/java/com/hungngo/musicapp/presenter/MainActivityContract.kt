package com.hungngo.musicapp.presenter

interface MainActivityContract {
    interface View {
        fun onGetListSongsSuccess(message: String)
    }

    interface Presenter {
        fun getListSongs()
    }
}
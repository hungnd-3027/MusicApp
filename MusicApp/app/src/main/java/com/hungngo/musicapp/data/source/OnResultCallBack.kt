package com.hungngo.musicapp.data.source

import com.hungngo.musicapp.data.model.Song

interface OnResultCallBack {
    fun onLoaded(songs: MutableList<Song>)
    fun onFailed()
}

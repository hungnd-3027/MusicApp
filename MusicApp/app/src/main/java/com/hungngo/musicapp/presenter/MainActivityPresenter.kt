package com.hungngo.musicapp.presenter

import com.hungngo.musicapp.data.model.Song
import com.hungngo.musicapp.data.reposity.SongRepository
import com.hungngo.musicapp.data.source.OnResultCallBack

class MainActivityPresenter(
    private val songRepository: SongRepository,
    private val view: MainActivityContract.View
) : MainActivityContract.Presenter {

    override fun getListSongs() {
        songRepository.getData(object : OnResultCallBack {
            override fun onLoaded(songs: MutableList<Song>) {
                view.onGetListSongsSuccess(songs)
            }

            override fun onFailed() {
                view.onGetListSongsFailed()
            }
        })
    }
}

package com.hungngo.musicapp.data.reposity

import com.hungngo.musicapp.data.source.local.task.IMusicDataSource
import com.hungngo.musicapp.data.source.OnResultCallBack

class SongRepository(private val musicsDataSource: IMusicDataSource) : IMusicDataSource {
    override fun getData(callback: OnResultCallBack) {
        musicsDataSource.getData(callback)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(musicsDataSource: IMusicDataSource) =
            instance ?: SongRepository(musicsDataSource).also { instance = it }
    }
}

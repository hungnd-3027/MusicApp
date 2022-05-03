package com.hungngo.musicapp.data.source.local.task

import com.hungngo.musicapp.data.source.OnResultCallBack

interface IMusicDataSource {
    fun getData(callback: OnResultCallBack)
}

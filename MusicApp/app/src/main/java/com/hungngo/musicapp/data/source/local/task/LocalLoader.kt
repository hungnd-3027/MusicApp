package com.hungngo.musicapp.data.source.local.task

import android.content.Context
import android.content.CursorLoader
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.hungngo.musicapp.data.model.Song
import com.hungngo.musicapp.data.source.OnResultCallBack

class LocalLoader(private val context: Context) : IMusicDataSource {
    private val songList = mutableListOf<Song>()
    private val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    private val contentUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    private val projection = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
    )

    override fun getData(callback: OnResultCallBack) {
        val loader = CursorLoader(
            context,
            contentUri,
            projection,
            selection,
            null,
            null
        )
        val cursor = loader.loadInBackground()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                songList.add(Song(cursor.getString(0), cursor.getString(1), cursor.getString(2)))
            }
        }

        songList.let {
            if (it.isNotEmpty()) {
                callback.onLoaded(it)
            } else {
                callback.onFailed()
            }
        }
    }

    companion object {
        private var instance: LocalLoader? = null
        fun getInstance(context: Context) =
            instance ?: LocalLoader(context).also { instance = it }
    }
}

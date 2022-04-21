package com.hungngo.musicapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hungngo.musicapp.service.MyMusicService

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionMusic = intent?.getStringExtra("action_music")
        Intent(context, MyMusicService::class.java).also {
            it.action = actionMusic
            context?.startService(it)
        }
    }
}
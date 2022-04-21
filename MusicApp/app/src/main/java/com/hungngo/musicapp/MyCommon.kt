package com.hungngo.musicapp

import com.hungngo.musicapp.model.Song

object MyCommon {
    val ACTION_PLAY = "play_music"
    val ACTION_PAUSE = "stop_music"
    val ACTION_RESUME = "resume_music"
    val ACTION_NEXT = "next_song"
    val ACTION_PREVIOUS = "previous_song"

    var listSongs = mutableListOf<Song>()

    const val CHANNEL_ID = "your_id"
    const val MUSIC_NOtiFICATION = 123

    //broadcast receiver
    val ACTION_SEND = "send_data"
    val ACTION_SEND_TOTAL = "send_total"


    val NOTI_CHANNEL_ID = "100"
    val NOTI_CHANNEL_NAME = "Hundnd79"
}

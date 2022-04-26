package com.hungngo.musicapp

object Constance {
    const val ACTION_PLAY = "com.hungngo.musicapp.play_music"
    const val ACTION_PAUSE = "com.hungngo.musicapp.stop_music"
    const val ACTION_RESUME = "com.hungngo.musicapp.resume_music"
    const val ACTION_NEXT = "com.hungngo.musicapp.next_song"
    const val ACTION_PREVIOUS = "com.hungngo.musicapp.previous_song"


    //broadcast receiver
    const val ACTION_SEND = "com.hungngo.musicapp.send_data"
    const val ACTION_SEND_TOTAL = "com.hungngo.musicapp.send_total"


    const val NOTIFICATION__ID = "100"
    const val NOTIFICATION_CHANNEL_ID = "100"
    const val NOTIFICATION_CHANNEL_NAME = "Hundnd79"

    const val BUNDLE_LIST_SONGS = "BUNDLE_SEND_LIST_SONGS"
    const val BUNDLE_POSITION = "BUNDLE_POSITION"
    const val BUNDLE_TIME = "BUNDLE_TIME"
    const val EXTRA_CURRENT_TIME = "com.hungngo.musicapp.EXTRA_CURRENT_TIME"
    const val EXTRA_MUSIC = "com.hungngo.musicapp.ACTION_MUSIC"
    const val BUNDLE_TOTAL = "BUNDLE_TOTAL"
    const val BUNDLE_STATUS = "BUNDLE_STATUS"
}

package com.hungngo.musicapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hungngo.musicapp.Constance
import com.hungngo.musicapp.MyReceiver
import com.hungngo.musicapp.R
import com.hungngo.musicapp.data.model.Song
import kotlin.random.Random

class MyMusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var position = 0
    private lateinit var listSongs: MutableList<Song>

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        if (bundle != null) {
            position = bundle.getInt(Constance.BUNDLE_POSITION, 0)
            listSongs = bundle.getSerializable(Constance.BUNDLE_LIST_SONGS) as MutableList<Song>

            val time = bundle.getInt(Constance.BUNDLE_TIME, 0)
            mediaPlayer?.seekTo(time)
        }

        handlerAction(intent?.action, position)

        return START_NOT_STICKY
    }

    private fun handlerAction(action: String?, position: Int) {
        Constance.apply {
            when (action) {
                ACTION_PLAY -> playMusic(position)
                ACTION_PAUSE -> pauseSong()
                ACTION_RESUME -> resumeSong()
                ACTION_NEXT -> nextSong()
                ACTION_PREVIOUS -> previousSong()
            }
        }
    }

    private fun pauseSong() {
        if (isPlaying) {
            mediaPlayer?.pause()
            isPlaying = false
            showNotification()
            sendDataToFragment()
        }
    }

    private fun resumeSong() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer?.currentPosition?.also {
                mediaPlayer?.seekTo(it)
                mediaPlayer?.start()
                isPlaying = true
                showNotification()
                sendDataToFragment()
            }
        }
    }

    private fun previousSong() {
        position -= 1
        if (position < 0) position = listSongs.size - 1
        playMusic(position)
    }

    private fun nextSong() {
        position = (position + 1) % listSongs.size
        playMusic(position)
    }

    private fun playMusic(position: Int) {
        if (isPlaying) {
            mediaPlayer?.stop()
        }
        mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(listSongs[position].uri))
        mediaPlayer?.start()

        isPlaying = true

        sendCurrentTime()
        sendDataToFragment()
        showNotification()
        Toast.makeText(this, "Playing ${listSongs[position].name}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun sendCurrentTime() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(Constance.ACTION_SEND)
                intent.putExtra(Constance.EXTRA_CURRENT_TIME, mediaPlayer?.currentPosition)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                mediaPlayer?.setOnCompletionListener {
                    nextSong()
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    private fun sendDataToFragment() {
        val intent = Intent(Constance.ACTION_SEND_TOTAL)
        val bundle = Bundle()
        mediaPlayer?.duration?.let {
            bundle.putInt(Constance.BUNDLE_TOTAL, it)
            bundle.putBoolean(Constance.BUNDLE_STATUS, isPlaying)
        }
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun showNotification() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.notification_music_bg)
        val mediaSessionCompat = MediaSessionCompat(this, MEDIA_SESSION_TAG)

        val notificationBuilder =
            NotificationCompat.Builder(this, Constance.NOTIFICATION__ID).apply {
                setContentTitle(listSongs[position].name)
                setContentText(listSongs[position].singer)
                setSmallIcon(R.drawable.ic_music)
                setLargeIcon(bitmap)
                addAction(
                    R.drawable.ic_previous,
                    getString(R.string.title_notification_action_previous),
                    getPendingIntent(this@MyMusicService, Constance.ACTION_PREVIOUS)
                ) // #0
                if (isPlaying) {
                    addAction(
                        R.drawable.ic_pause_music,
                        getString(R.string.title_notification_action_pause),
                        getPendingIntent(this@MyMusicService, Constance.ACTION_PAUSE)
                    ) // #1
                } else {
                    addAction(
                        R.drawable.ic_play_music,
                        getString(R.string.title_notification_action_resume),
                        getPendingIntent(this@MyMusicService, Constance.ACTION_RESUME)
                    ) // #1
                }
                addAction(
                    R.drawable.ic_next,
                    getString(R.string.title_notification_action_next),
                    getPendingIntent(this@MyMusicService, Constance.ACTION_NEXT)
                ) // #2
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSessionCompat.sessionToken)
                )
            }

        startForeground(FOREGROUND_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        Intent(context, MyReceiver::class.java).also {
            it.putExtra(Constance.EXTRA_MUSIC, action)
            it.action = action
            return PendingIntent.getBroadcast(
                context.applicationContext,
                Random.nextInt(),
                it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constance.NOTIFICATION_CHANNEL_ID,
                Constance.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val FOREGROUND_NOTIFICATION_ID = 1
        const val MEDIA_SESSION_TAG = "MEDIA_SESSION_TAG"
    }
}

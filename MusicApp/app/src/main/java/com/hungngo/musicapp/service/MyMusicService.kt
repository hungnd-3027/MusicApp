package com.hungngo.musicapp.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hungngo.musicapp.MyCommon
import com.hungngo.musicapp.MyReceiver
import com.hungngo.musicapp.R
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class MyMusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var position = 0

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
            position = bundle.getInt("position", 0)
        }

        handlerAction(intent?.action, position)

        val time: Int = intent!!.getIntExtra("time", 0)
        mediaPlayer?.seekTo(time)

        return START_NOT_STICKY
    }

    private fun handlerAction(action: String?, position: Int) {
        when (action) {
            MyCommon.ACTION_PLAY -> {
                playMusic(position)
            }
            MyCommon.ACTION_PAUSE -> {
                pauseSong()
            }
            MyCommon.ACTION_RESUME -> {
                resumeSong()
            }
            MyCommon.ACTION_NEXT -> {
                nextSong()
            }
            MyCommon.ACTION_PREVIOUS -> {
                previousSong()
            }
        }
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        isPlaying = false
        showNotification()
    }

    private fun resumeSong() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer!!.start()
            isPlaying = true
            showNotification()
        }
    }

    private fun previousSong() {
        position -= 1
        if (position < 0) position = MyCommon.listSongs.size - 1
        playMusic(position)
    }

    private fun nextSong() {
        position = (position + 1) % MyCommon.listSongs.size
        playMusic(position)
    }

    private fun playMusic(position: Int) {
        if (isPlaying) {
            mediaPlayer!!.stop()
        }
        mediaPlayer = MediaPlayer.create(applicationContext, MyCommon.listSongs[position].uri)
        mediaPlayer!!.start()

        isPlaying = true

        sendCurrentTime()
        sendDataToFragment()
        showNotification()
        Toast.makeText(this, "Playing ${MyCommon.listSongs[position].name}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun sendCurrentTime() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(MyCommon.ACTION_SEND)
                intent.putExtra("current_time", mediaPlayer!!.currentPosition)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                mediaPlayer!!.setOnCompletionListener {
                    nextSong()
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    private fun sendDataToFragment() {
        val intent = Intent(MyCommon.ACTION_SEND_TOTAL)
        val bundle = Bundle()
        bundle.putInt("total", mediaPlayer!!.duration)
        bundle.putBoolean("status", isPlaying)
        intent.putExtras(bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_notification_music)
        val mediaSessionCompat = MediaSessionCompat(this, "tag")

        val notificationBuilder = NotificationCompat.Builder(this, "100")
            .setContentTitle(MyCommon.listSongs[position].name)
            .setContentText("My Awesome Band")
            .setSmallIcon(R.drawable.ic_music)
            .setLargeIcon(bitmap)
            .addAction(
                R.drawable.ic_previous,
                "Previous",
                getPendingIntent(this, MyCommon.ACTION_PREVIOUS)
            ) // #0
        if (isPlaying) {
            notificationBuilder.addAction(
                R.drawable.ic_pause_music,
                "Pause",
                getPendingIntent(this, MyCommon.ACTION_PAUSE)
            ) // #1
        } else {
            notificationBuilder.addAction(
                R.drawable.ic_play_music,
                "Resume",
                getPendingIntent(this, MyCommon.ACTION_RESUME)
            ) // #1

        }
            .addAction(
                R.drawable.ic_next,
                "Next",
                getPendingIntent(this, MyCommon.ACTION_NEXT)
            ) // #2
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1)
                    .setMediaSession(mediaSessionCompat.sessionToken)
            )

        startForeground(1, notificationBuilder.build())
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        Intent(context, MyReceiver::class.java).also {
            it.putExtra("action_music", action)
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
                MyCommon.NOTI_CHANNEL_ID,
                MyCommon.NOTI_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)
        }
    }
}
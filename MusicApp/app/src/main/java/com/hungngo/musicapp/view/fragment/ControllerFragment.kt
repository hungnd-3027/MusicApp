package com.hungngo.musicapp.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hungngo.musicapp.Constance
import com.hungngo.musicapp.R
import com.hungngo.musicapp.data.model.Song
import com.hungngo.musicapp.service.MyMusicService
import kotlinx.android.synthetic.main.fragment_controller.*
import kotlinx.android.synthetic.main.fragment_controller.view.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

private const val BUNDLE_LIST_SONGS = "BUNDLE_LIST_SONGS"

class ControllerFragment : Fragment(), View.OnClickListener {
    private var isPlaying: Boolean = true
    private var listSongs: MutableList<Song>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listSongs = it.getSerializable(BUNDLE_LIST_SONGS) as MutableList<Song>?
        }
    }
    private val mTimeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            if (intent.action.equals(Constance.ACTION_SEND)) {
                val data = intent.getIntExtra(Constance.EXTRA_CURRENT_TIME, 0)
                val format = SimpleDateFormat("mm:ss",Locale.getDefault())
                text_elapsed_time.text = format.format(data)
                sb_song.progress = data
            }
        }
    }

    private val mTotalReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            if (intent.action.equals(Constance.ACTION_SEND_TOTAL)) {
                val bundle = intent.extras
                if (bundle != null) {
                    val total = bundle.getInt(Constance.BUNDLE_TOTAL)
                    isPlaying = bundle.getBoolean(Constance.BUNDLE_STATUS, true)

                    if (isPlaying){
                        fab_play.setImageResource(R.drawable.ic_stop_music)
                    }else{
                        fab_play.setImageResource(R.drawable.ic_play_music)
                    }
                    val format = SimpleDateFormat("mm:ss", Locale.getDefault())
                    text_remaining_time?.text = format.format(total)
                    sb_song?.max = total
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_controller, container, false)

        LocalBroadcastManager.getInstance(view.context)
            .registerReceiver(mTimeReceiver, IntentFilter(Constance.ACTION_SEND))

        LocalBroadcastManager.getInstance(view.context)
            .registerReceiver(mTotalReceiver, IntentFilter(Constance.ACTION_SEND_TOTAL))

        view.fab_play.setOnClickListener(this)
        view.fab_next.setOnClickListener(this)
        view.fab_previous.setOnClickListener(this)

        view.sb_song.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val intent = Intent(context, MyMusicService::class.java)
                val bundle = Bundle()
                bundle.putSerializable(Constance.BUNDLE_LIST_SONGS, listSongs as Serializable)
                intent.putExtra(Constance.BUNDLE_TIME, seekBar?.progress)
                intent.putExtras(bundle)
                context?.startService(intent)
            }
        })

        return view
    }

    override fun onClick(v: View?) {
        val intent = Intent(context, MyMusicService::class.java)
        when (v?.id) {
            R.id.fab_play -> {
                clickButtonPlay(intent)
            }
            R.id.fab_next -> {
                fab_play.setImageResource(R.drawable.ic_stop_music)
                intent.action = Constance.ACTION_NEXT
            }
            R.id.fab_previous -> {
                fab_play.setImageResource(R.drawable.ic_stop_music)
                intent.action = Constance.ACTION_PREVIOUS
            }
        }
        context?.startService(intent)
    }

    private fun clickButtonPlay(intent: Intent) {
        if (isPlaying) {
            fab_play.setImageResource(R.drawable.ic_play_music)
            intent.action = Constance.ACTION_PAUSE
            isPlaying = false
        } else {
            fab_play.setImageResource(R.drawable.ic_stop_music)
            intent.action = Constance.ACTION_RESUME
            isPlaying = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let {
            LocalBroadcastManager.getInstance(it).apply {
                unregisterReceiver(mTimeReceiver)
                unregisterReceiver(mTotalReceiver)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(listSongs: MutableList<Song>?) = ControllerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(BUNDLE_LIST_SONGS, listSongs as Serializable)
            }
        }
    }
}

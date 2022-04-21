package com.hungngo.musicapp.view.fragment

import android.annotation.SuppressLint
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
import com.hungngo.musicapp.MyCommon
import com.hungngo.musicapp.R
import com.hungngo.musicapp.service.MyMusicService
import kotlinx.android.synthetic.main.fragment_controller.*
import kotlinx.android.synthetic.main.fragment_controller.view.*
import java.text.SimpleDateFormat

private const val ARG_PARAM1 = "bundle"

class ControllerFragment : Fragment(), View.OnClickListener {
    private var isPlaying: Boolean? = true
    private var bundle: Bundle? = null


    private val mTimeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SimpleDateFormat")
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            if (intent.action.equals(MyCommon.ACTION_SEND)) {
                val data = intent.getIntExtra("current_time", 0)
                val format = SimpleDateFormat("mm:ss")
                tv_elapsed_time.text = format.format(data)
                sb_song.progress = data
            }
        }
    }

    private val mTotalReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SimpleDateFormat")
        override fun onReceive(context: Context?, intent: Intent) {
            // Get extra data included in the Intent
            if (intent.action.equals(MyCommon.ACTION_SEND_TOTAL)) {
                val bundle = intent.extras
                if (bundle != null) {
                    val total = bundle.getInt("total")
                    isPlaying = bundle.getBoolean("status", true)

                    val format = SimpleDateFormat("mm:ss")
                    tv_remaining_time?.text = format.format(total)
                    sb_song?.max = total
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it.getBundle(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_controller, container, false)

        LocalBroadcastManager.getInstance(view.context)
            .registerReceiver(mTimeReceiver, IntentFilter(MyCommon.ACTION_SEND))

        LocalBroadcastManager.getInstance(view.context)
            .registerReceiver(mTotalReceiver, IntentFilter(MyCommon.ACTION_SEND_TOTAL))

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
                intent.putExtra("time", seekBar?.progress)
                context?.startService(intent)
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ControllerFragment().apply {
                arguments = Bundle().apply {
                    putBundle(ARG_PARAM1, bundle)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(mTimeReceiver) }
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(mTotalReceiver) }
    }

    override fun onClick(v: View?) {
        val intent = Intent(context, MyMusicService::class.java)
        when (v!!.id) {
            R.id.fab_play -> {
                clickButtonPlay(intent)
            }
            R.id.fab_next -> {
                fab_play.setImageResource(R.drawable.ic_stop_music)
                intent.action = MyCommon.ACTION_NEXT
            }
            R.id.fab_previous -> {
                fab_play.setImageResource(R.drawable.ic_stop_music)
                intent.action = MyCommon.ACTION_PREVIOUS
            }
        }
        context?.startService(intent)
    }

    private fun clickButtonPlay(intent: Intent) {
        if (isPlaying!!) {
            fab_play.setImageResource(R.drawable.ic_play_music)
            intent.action = MyCommon.ACTION_PAUSE
            isPlaying = false
        } else {
            fab_play.setImageResource(R.drawable.ic_stop_music)
            intent.action = MyCommon.ACTION_RESUME
            isPlaying = true
        }
    }
}
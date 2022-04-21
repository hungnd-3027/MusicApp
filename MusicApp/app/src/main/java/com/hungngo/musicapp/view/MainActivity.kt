package com.hungngo.musicapp.view


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hungngo.musicapp.MyCommon
import com.hungngo.musicapp.R
import com.hungngo.musicapp.presenter.MainActivityContract
import com.hungngo.musicapp.presenter.MainActivityPresenter
import com.hungngo.musicapp.service.MyMusicService
import com.hungngo.musicapp.view.adapter.SongAdapter
import com.hungngo.musicapp.view.fragment.ControllerFragment
import com.hungngo.musicapp.view.fragment.ListSongsFragment


class MainActivity : AppCompatActivity(), SongAdapter.SendDataInterface, MainActivityContract.View {
    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)

        addFragment(ListSongsFragment.newInstance())

        getListSongs()
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frl_list_song, fragment, "fragmentSongs").commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frl_controller_song, fragment, "fragmentSongs").commit()
    }

    private fun getListSongs() {
        presenter.getListSongs()
    }

    override fun sendDataToPlay(position: Int) {
        val bundle = Bundle()
        bundle.putInt("position", position)

        replaceFragment(ControllerFragment.newInstance(bundle))

        Intent(this, MyMusicService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.action = MyCommon.ACTION_PLAY
                it.putExtras(bundle)
                startForegroundService(it)
            } else {
                startService(it)
            }
        }
    }

    override fun onGetListSongsSuccess(message: String) {
    }
}



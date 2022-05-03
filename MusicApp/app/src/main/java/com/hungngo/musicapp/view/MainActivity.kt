package com.hungngo.musicapp.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.hungngo.musicapp.Constance
import com.hungngo.musicapp.R
import com.hungngo.musicapp.data.model.Song
import com.hungngo.musicapp.presenter.MainActivityContract
import com.hungngo.musicapp.presenter.MainActivityPresenter
import com.hungngo.musicapp.data.reposity.SongRepository
import com.hungngo.musicapp.service.MyMusicService
import com.hungngo.musicapp.data.source.local.task.LocalLoader
import com.hungngo.musicapp.view.adapter.SongAdapter
import com.hungngo.musicapp.view.fragment.ControllerFragment
import com.hungngo.musicapp.view.fragment.ListSongsFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(), SongAdapter.SendDataInterface, MainActivityContract.View {
    private lateinit var presenter: MainActivityPresenter
    private var listSong: MutableList<Song>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainActivityPresenter(
            SongRepository.getInstance(LocalLoader.getInstance(this)),
            this
        )
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000
                )
                return
            }
        }
        getListSongs()
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_list_song, fragment).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_controller_song, fragment).commit()
    }

    private fun getListSongs() {
        presenter.getListSongs()
    }

    override fun sendDataToPlay(position: Int) {
        val bundle = Bundle()
        bundle.putInt(Constance.BUNDLE_POSITION, position)
        bundle.putSerializable(Constance.BUNDLE_LIST_SONGS, listSong as Serializable)

        replaceFragment(ControllerFragment.newInstance(listSong))

        Intent(this, MyMusicService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.action = Constance.ACTION_PLAY
                it.putExtras(bundle)
                startForegroundService(it)
            } else {
                it.action = Constance.ACTION_PLAY
                it.putExtras(bundle)
                startService(it)
            }
        }
    }

    override fun setListSongHeight() {
        val initHeight = this.frame_list_song.height
        // set height of ListSongsFragment
        val frlList = this.frame_list_song

        val frlController = this.frame_controller_song
        //  newHeight = currentHeight of ListSongsFragment - currentHeight of ControllerFragment
        if (frlList.height > (initHeight - frlController.height)) {
            frlList.layoutParams.height = frlList.height - frlController.height
        }
    }

    override fun onGetListSongsSuccess(songs: MutableList<Song>) {
        listSong = songs
        addFragment(ListSongsFragment.newInstance(songs))
    }

    override fun onGetListSongsFailed() {
        Toast.makeText(this, getString(R.string.error_load_song_failed), Toast.LENGTH_SHORT).show()
    }
}

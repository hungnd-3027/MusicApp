package com.hungngo.musicapp.presenter

import com.hungngo.musicapp.MyCommon
import com.hungngo.musicapp.R
import com.hungngo.musicapp.model.Song

class MainActivityPresenter(private val view: MainActivityContract.View) : MainActivityContract.Presenter {

    override fun getListSongs() {
        MyCommon.listSongs.add(Song("Thức giấc", R.raw.thucgiac_dalab))
        MyCommon.listSongs.add(Song("Hẹn kiếp sau", R.raw.henkiepsau_khahiep))
        MyCommon.listSongs.add(Song("Thê lương", R.raw.theluong_phucchinh))
        MyCommon.listSongs.add(Song("Danh phận", R.raw.danhphan_jangmi))
        MyCommon.listSongs.add(Song("Thức giấc", R.raw.thucgiac_dalab))
        MyCommon.listSongs.add(Song("Hẹn kiếp sau", R.raw.henkiepsau_khahiep))
        MyCommon.listSongs.add(Song("Thê lương", R.raw.theluong_phucchinh))
        MyCommon.listSongs.add(Song("Danh phận", R.raw.danhphan_jangmi))
        view.onGetListSongsSuccess("Get songs successfully")
    }

}
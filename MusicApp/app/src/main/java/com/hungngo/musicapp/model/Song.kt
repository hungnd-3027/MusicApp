package com.hungngo.musicapp.model

class Song(var name : String, var uri : Int) {

    override fun toString(): String {
        return "$name"
    }
}
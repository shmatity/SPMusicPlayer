package com.musicplayer.spanova.spmusicplayer.artist;

import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class Artist {

    String name = "";
    List<Song> songList;

    public Artist(String name, List<Song> songList) {
        this.name = name;
        this.songList = songList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}

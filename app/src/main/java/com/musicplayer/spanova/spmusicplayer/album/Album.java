package com.musicplayer.spanova.spmusicplayer.album;

import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class Album {
    String name="";
    List<Song> currentArtistSongList;

    public Album(String name, List<Song> currentArtistSongList) {
        this.name = name;
        this.currentArtistSongList = currentArtistSongList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getCurrentArtistSongList() {
        return currentArtistSongList;
    }

    public void setCurrentArtistSongList(List<Song> currentArtistSongList) {
        this.currentArtistSongList = currentArtistSongList;
    }

}

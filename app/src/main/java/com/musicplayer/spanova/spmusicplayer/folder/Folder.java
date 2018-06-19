package com.musicplayer.spanova.spmusicplayer.folder;

import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class Folder {
    String name="";
    String path="";
    List<Song> songList;

    public Folder(String name, String path, List<Song> songList) {
        this.name = name;
        this.path = path;
        this.songList = songList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }


}

package com.musicplayer.spanova.spmusicplayer;

class Song {
    String title = "None";
    String artist = "Unknown";
    String secondaryArtists = "Unknown";
    String album = "None";
    String art = "undefined";
    int year = 0;
    String uri = "/";

    public Song(String title, String artist, String secondaryArtists, String album, String art, int year, String uri) {
        this.title = title;
        this.artist = artist;
        this.secondaryArtists = secondaryArtists;
        this.album = album;
        this.art = art;
        this.year = year;
        this.uri = uri;
    }

    public Song(String title, String artist, String uri) {
        this.title = title;
        this.artist = artist;
        this.uri = uri;
    }
}

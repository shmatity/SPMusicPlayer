package com.musicplayer.spanova.spmusicplayer.song;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import com.musicplayer.spanova.spmusicplayer.R;

import java.io.Serializable;

public class Song implements Serializable {

    String title = "None";
    String artist = "Unknown";
    String secondaryArtists = "Unknown";
    String album = "None";
    String art = "undefined";
    String uri = "/";
    Bitmap image = null;
    int year = 0;

    public Song(String title, String artist, String secondaryArtists, String album, int year, String uri) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImageFromSong(String uri, Resources res) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = BitmapFactory.decodeResource( res,
                R.drawable.ic_launcher_background);
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(uri);
        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

        return art;
    }
}

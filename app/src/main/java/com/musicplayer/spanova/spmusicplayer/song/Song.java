package com.musicplayer.spanova.spmusicplayer.song;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.utils.Utils;

import java.io.Serializable;

public class Song implements Serializable {
    int id = 0;
    String title = "None";
    String artist = "Unknown";
    String secondaryArtists = "Unknown";
    String album = "None";
    String art = "undefined";
    String uri = "/";
    Bitmap image = null;
    int year = 0;

    public Song(int id, String title, String artist, String secondaryArtists, String album, int year, String uri) {
        this.id = id;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImageFromSong(Context ctx) {
        MediaMetadataRetriever  metaRetriver = new MediaMetadataRetriever();
        byte[] art = null;
        Bitmap songImage = Utils.getBitmapFromVectorDrawable(ctx, R.drawable.ic_music_black);
        metaRetriver.setDataSource(this.uri);
        try {
            art = metaRetriver.getEmbeddedPicture();
            songImage = BitmapFactory
                    .decodeByteArray(art, 0, art.length);
        } catch (Exception e) {
            Log.d("SP", e.getMessage());
        }
        return songImage;
    }
}

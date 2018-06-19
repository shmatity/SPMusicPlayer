package com.musicplayer.spanova.spmusicplayer.controller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.SurfaceView;
import android.view.View;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class CpmpactMusicPlayer {

    private MusicService musicSrv;
    private Intent playIntent;
    Context context;
    Activity activity;
    CollectDataEventListener onDataCollected;
    List<Song> songList= null;
    int sort = 0;
    String search= "";

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.setSearch(search);
            musicSrv.setSortOption(sort);
            musicSrv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });
            songList = musicSrv.getSongList();
            onDataCollected.run(context, musicSrv);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public CpmpactMusicPlayer(Context c, Activity activity, boolean b){
        this.context = c;
        this.activity = activity;
    }

    public void connect() {
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    public void setOnDataCollected(CollectDataEventListener onDataCollected) {
        this.onDataCollected = onDataCollected;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void showForSongOnPosition(int position) {
        updateInfo();
        musicSrv.setCurrentSongIndex(position);
        musicSrv.playSong();
        activity.findViewById(R.id.surfaceContainer).setVisibility(View.VISIBLE);

    }

    public void updateInfo() {
        activity.findViewById(R.id.surfaceContainer).setVisibility(View.VISIBLE);
    }
}

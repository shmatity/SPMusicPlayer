package com.musicplayer.spanova.spmusicplayer.controller;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import com.musicplayer.spanova.spmusicplayer.MainActivity;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.song.SongAdapter;

import java.util.List;

public class MusicController extends MediaController implements SurfaceHolder.Callback, MediaController.MediaPlayerControl {

    private MusicService musicSrv;
    private Intent playIntent;
    SurfaceView videoSurface;
    Context context;
    Activity activity;
    CollectDataEventListener onDataCollected;

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
            onDataCollected.run(context, musicSrv);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public MusicController(Context c, Activity activity, boolean b){
        super(activity, b);
        this.context = getContext();
        this.activity = activity;
    }

    public void connect() {
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    public void hide( ){ }

    public void hide(boolean hide){
        if(hide) this.hide();
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Song> getSongList() {
        return musicSrv.getSongList();
    }

    public void showForSongOnPosition(int position) {
        videoSurface = (SurfaceView) activity.findViewById(R.id.surface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        musicSrv.setCurrentSongIndex(position);
        musicSrv.playSong();
        this.setMediaPlayer(this);
        this.setAnchorView(activity.findViewById(R.id.listView1));
        this.setEnabled(true);
        this.show();
    }

    public void setOnDataCollected(CollectDataEventListener collectDataEventListener) {
        onDataCollected = collectDataEventListener;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        musicSrv.setDisplay(holder);
        musicSrv.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
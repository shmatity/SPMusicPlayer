package com.musicplayer.spanova.spmusicplayer.controller;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;

public class MusicController_ implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, CustomControllerView.MediaPlayerControl {

    SurfaceView surface;
    MusicService player;
    Activity activity;
    CustomControllerView controller;

    public MusicController_(Context context, boolean b, MusicService player, Activity activity){
        this.player = player;
        this.activity = activity;
        surface = (SurfaceView) activity.findViewById(R.id.surface);
        SurfaceHolder videoHolder = surface.getHolder();
        videoHolder.addCallback(this);

        controller = new CustomControllerView(context);



//        try {
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//            player.setOnPreparedListener(this);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        controller.show();
//        return false;
//    }
public void setSongIndex(int index) {
    player.setCurrentSongIndex(index);
    player.playSong();
    controller.show();
}

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) activity.findViewById(R.id.surfaceContainer));
        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

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
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
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
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

}
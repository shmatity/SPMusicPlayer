package com.musicplayer.spanova.spmusicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;

public class MusicEventsReceiverHelper {
    static MusicEventsReceiverHelper musicEventsReceiverHelper = new MusicEventsReceiverHelper();
    MusicService musicSrv = null;
    String action;
    Context context;
    TaskListener onPlayPause;
    TaskListener onPrev;
    TaskListener onNext;
    TaskListener onShuffle;
    TaskListener onRepeat;
    TaskListener onArtClicked;
    TaskListener onUpdateAll;
    TaskListener onStop;

    public static MusicEventsReceiverHelper getInstance() {
        return musicEventsReceiverHelper;
    }

    public void setOnPlayPause(TaskListener onPlayPause) {
        this.onPlayPause = onPlayPause;
    }

    public void setOnPrev(TaskListener onPrev) {
        this.onPrev = onPrev;
    }

    public void setOnNext(TaskListener onNext) {
        this.onNext = onNext;
    }

    public void setOnShuffle(TaskListener onShuffle) {
        this.onShuffle = onShuffle;
    }

    public void setOnRepeat(TaskListener onRepeat) {
        this.onRepeat = onRepeat;
    }

    public void setOnArtCliced(TaskListener onArtClicked) {
        this.onArtClicked = onArtClicked;
    }

    public void setOnUpdateAll(TaskListener onUpdateAll) {
        this.onUpdateAll = onUpdateAll;
    }

    public void setOnStop(TaskListener onStop) {
        this.onStop = onStop;
    }


    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            if (musicSrv == null) {
                musicSrv = binder.getService();

//                musicSrv.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        onPlayPause.run(context, musicSrv);
//                    }
//                });
                pickAction();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    public void onReceive(Context context, String action) {
        this.action = action;
        this.context = context;

        Intent play = new Intent(context, MusicService.class);
        context.getApplicationContext().bindService(play, musicConnection,
                Context.BIND_AUTO_CREATE);
        if (musicSrv != null) {
            pickAction();
        }
    }

    private void pickAction() {
        if (Constants.PLAY_PAUSE_ACTION.equals(action)) {
            if (musicSrv.isPlaying()) {
                musicSrv.pause();
            } else {
                musicSrv.start();
            }
            if (onPlayPause != null) onPlayPause.run(context, musicSrv);
        } else if (Constants.NEXT_ACTION.equals(action)) {
            musicSrv.playNext();
            if (onNext != null) onNext.run(context, musicSrv);
        } else if (Constants.PREV_ACTION.equals(action)) {
            musicSrv.playPrev();
            if (onPrev != null) onPrev.run(context, musicSrv);
        } else if (Constants.SHUFFLE_ACTION.equals(action)) {
            musicSrv.setShuffle();
            if (onShuffle != null) onShuffle.run(context, musicSrv);
        } else if (Constants.REPEAT_ACTION.equals(action)) {
            musicSrv.setRepeat();
            if (onRepeat != null) onRepeat.run(context, musicSrv);
        } else if (Constants.ART_CLICKED_ACTION.equals(action)) {
            //TBD add change art functionality
            if (onArtClicked != null) onArtClicked.run(context, musicSrv);
        } else if (Constants.STOP_ACTION.equals(action)) {
            musicSrv.stop();
            if (onStop != null) onStop.run(context, musicSrv);
        } else if (Constants.UPDATE_ALL_ACTION.equals(action)) {
            //currently only for widget
            if (onUpdateAll != null) onUpdateAll.run(context, musicSrv);
        } else {
            new Error("WE ARE FUCKED! NOT SUCK ACTION");
        }
    }

}

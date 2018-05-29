package com.musicplayer.spanova.spmusicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;

public class MusicEventsReceiver extends BroadcastReceiver {
    MusicService musicSrv;
    String action;
    Context context;
    TaskListener onPlayPause;
    TaskListener onPrev;
    TaskListener onNext;
    TaskListener onShuffle;
    TaskListener onRepeat;
    TaskListener onArtClicked;


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

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();

            if(Constants.PLAY_PAUSE_ACTION.equals(action)) {
                if (musicSrv.isPlaying()){
                    musicSrv.pause();
                } else {
                    musicSrv.start();
                }
                onPlayPause.run(context, musicSrv);

            } else if(Constants.NEXT_ACTION.equals(action)) {
                musicSrv.playNext();
                onNext.run(context, musicSrv);
            } else if(Constants.PREV_ACTION.equals(action)) {
                musicSrv.playPrev();
                onPrev.run(context,musicSrv);
            } else if(Constants.SHUFFLE_ACTION.equals(action)) {
                musicSrv.setShuffle();
                onShuffle.run(context,musicSrv);
            } else if(Constants.REPEAT_ACTION.equals(action)) {
                musicSrv.setRepeat();
                onRepeat.run(context,musicSrv);
            } else if(Constants.ART_CLICKED_ACTION.equals(action)) {
                onArtClicked.run(context, musicSrv);
            } else {
                new Error("WE ARE FUCKED! NOT SUCK ACTION");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.action = action;
        this.context = context;
        Intent play = new Intent(context, MusicService.class);
        context.getApplicationContext().bindService(play, musicConnection,
                Context.BIND_AUTO_CREATE);
    }

}

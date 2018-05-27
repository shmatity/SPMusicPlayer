package com.musicplayer.spanova.spmusicplayer.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;

public class NotificationReceiver extends BroadcastReceiver {
    MusicService musicSrv;
    String action;
    Context context;
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();

            if("PLAY".equals(action)) {
                if (musicSrv.isPlaying()){
                    musicSrv.pause();
                } else {
                    musicSrv.start();
                }
                            CustomNotification.getInstance().updateNotification(context,
                    musicSrv.getSong().getTitle(),
                    musicSrv.getSong().getArtist(),
                    R.drawable.ic_music_black,
                    musicSrv.getSong().getImageFromSong(context),
                    musicSrv.isPlaying());

            } else if("NEXT".equals(action)) {
               musicSrv.playNext();
            } else if("PREV".equals(action)) {
                musicSrv.playPrev();
            }

//            CustomNotification.getInstance().updateNotification(context,
//                    musicSrv.getSong().getTitle(),
//                    musicSrv.getSong().getArtist(),
//                    R.drawable.ic_music_black,
//                    musicSrv.getSong().getImageFromSong(context),
//                    musicSrv);
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

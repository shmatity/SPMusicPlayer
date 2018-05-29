package com.musicplayer.spanova.spmusicplayer.notification;

import android.content.Context;
import android.content.Intent;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.receiver.MusicEventsReceiver;
import com.musicplayer.spanova.spmusicplayer.receiver.TaskListener;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;

public class NotificationReceiver extends MusicEventsReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context,intent);
        super.setOnNext(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
            }
        });
        super.setOnPlayPause(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                showNotification(context, ms);
            }
        });
        super.setOnPrev(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
    }

    public void showNotification (Context context, MusicService musicSrv) {
        CustomNotification.getInstance().updateNotification(context,
                musicSrv.getSong().getTitle(),
                musicSrv.getSong().getArtist(),
                R.drawable.ic_music_black,
                musicSrv.getSong().getImageFromSong(context),
                musicSrv.isPlaying());
    }
}

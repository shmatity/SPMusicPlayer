package com.musicplayer.spanova.spmusicplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.receiver.MusicEventsReceiverHelper;
import com.musicplayer.spanova.spmusicplayer.receiver.TaskListener;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;

public class NotificationReceiver extends BroadcastReceiver {
    MusicEventsReceiverHelper musicEventsReceiverHelper = MusicEventsReceiverHelper.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {

        musicEventsReceiverHelper.setOnPlayPause(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                showNotification(context, ms);
            }
        });
        musicEventsReceiverHelper.onReceive(context, intent.getAction().replace(Constants.NOTIFICATION_TAG, ""));
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

package com.musicplayer.spanova.spmusicplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.musicplayer.spanova.spmusicplayer.MusicService;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if("PLAY".equals(action)) {
           Intent play = new Intent(context, MusicService.class);
            context.startService(play);
        } else if("NEXT".equals(action)) {
            Log.v("shuffTest","Pressed NO");
        } else if("PREV".equals(action)) {
            Log.v("shuffTest","Pressed MAYBE");
        }
    }
}

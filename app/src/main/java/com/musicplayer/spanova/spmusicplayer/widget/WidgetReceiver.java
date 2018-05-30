package com.musicplayer.spanova.spmusicplayer.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.receiver.MusicEventsReceiverHelper;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.receiver.TaskListener;

public class WidgetReceiver extends BroadcastReceiver {
    MusicEventsReceiverHelper musicEventsReceiverHelper = MusicEventsReceiverHelper.getInstance();
    @Override
    public void onReceive(final Context context, final Intent intent) {
        musicEventsReceiverHelper.setOnNext(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        musicEventsReceiverHelper.setOnPlayPause(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        musicEventsReceiverHelper.setOnPrev(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        musicEventsReceiverHelper.setOnShuffle(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {


            }
        });
        musicEventsReceiverHelper.setOnRepeat(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        musicEventsReceiverHelper.setOnArtCliced(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        musicEventsReceiverHelper.setOnUpdateAll(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {


            }
        });

        musicEventsReceiverHelper.onReceive(context,intent.getAction().replace(Constants.WIDGET_TAG, ""));
    }

    public void updateWidget(Context context) {
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
//        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);
//
//        RemoteViews remoteViews = new RemoteViews(context
//                .getApplicationContext().getPackageName(),
//                R.layout.new_app_widget);
//        remoteViews.setBitmap(R.id.songArt,"sdf" , ms.getSong().getImageFromSong(context));
//        remoteViews.setString(R.id.songTitle,"sdf" , ms.getSong().getTitle());
//        remoteViews.setString(R.id.songArtist,"sdf" , ms.getSong().getArtist());
    }
}

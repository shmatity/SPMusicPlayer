package com.musicplayer.spanova.spmusicplayer.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.receiver.MusicEventsReceiver;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.receiver.TaskListener;

public class WidgetReceiver extends MusicEventsReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context,intent);

        super.setOnNext(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        super.setOnPlayPause(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);

                RemoteViews remoteViews = new RemoteViews(context
                        .getApplicationContext().getPackageName(),
                        R.layout.new_app_widget);
                if(ms.isPlaying()) {
                    remoteViews.setImageViewResource(R.id.playPause, R.drawable.ic_play_black);
                } else {
                    remoteViews.setImageViewResource(R.id.playPause, R.drawable.ic_pause_black);
                }
                appWidgetManager.updateAppWidget(intent.getExtras().getInt(Constants.widgetID), remoteViews);
            }
        });
        super.setOnPrev(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {

            }
        });
        super.setOnShuffle(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);

                RemoteViews remoteViews = new RemoteViews(context
                        .getApplicationContext().getPackageName(),
                        R.layout.new_app_widget);
                if(ms.getShuffle()) {
                    remoteViews.setImageViewResource(R.id.shuffle, R.drawable.ic_shuffle_black);
                } else {
                    remoteViews.setImageViewResource(R.id.shuffle, R.drawable.ic_shuffle_gray);
                }
                appWidgetManager.updateAppWidget(intent.getExtras().getInt(Constants.widgetID), remoteViews);

            }
        });
        super.setOnRepeat(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);

                RemoteViews remoteViews = new RemoteViews(context
                        .getApplicationContext().getPackageName(),
                        R.layout.new_app_widget);
                    remoteViews.setImageViewResource(R.id.repeat, ms.getRepeatImage());
                appWidgetManager.updateAppWidget(intent.getExtras().getInt(Constants.widgetID), remoteViews);
            }
        });
        super.setOnArtCliced(new TaskListener() {
            @Override
            public void run(Context context, MusicService ms) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);

                RemoteViews remoteViews = new RemoteViews(context
                        .getApplicationContext().getPackageName(),
                        R.layout.new_app_widget);
                remoteViews.setImageViewResource(R.id.repeat, ms.getRepeatImage());
                appWidgetManager.updateAppWidget(intent.getExtras().getInt(Constants.widgetID), remoteViews);
            }
        });
    }

    public void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), NewAppWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(context
                        .getApplicationContext().getPackageName(),
                        R.layout.new_app_widget);
//                remoteViews.setImageViewResource(R.id.playPause, );
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }
}

package com.musicplayer.spanova.spmusicplayer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.notification.NotificationReceiver;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.receiver.MusicEventsReceiver;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final int INTENT_FLAGS = PendingIntent.FLAG_UPDATE_CURRENT;
    private static final int REQUEST_CODE = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


//        Context c = context.getApplicationContext();
//        c.registerReceiver(this, filter1);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        Intent shuffleIntent = new Intent(context, WidgetReceiver.class);
        shuffleIntent.putExtra(Constants.widgetID, appWidgetId);
        shuffleIntent.setAction(Constants.SHUFFLE_ACTION);
        PendingIntent shufflePendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, shuffleIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.shuffle, shufflePendingIntent);

        Intent playPauseIntent = new Intent(context, WidgetReceiver.class);
        playPauseIntent.putExtra(Constants.widgetID, appWidgetId);
        playPauseIntent.setAction(Constants.PLAY_PAUSE_ACTION);
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, playPauseIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.playPause, playPausePendingIntent);

        Intent prevIntent = new Intent(context, WidgetReceiver.class);
        prevIntent.setAction(Constants.PREV_ACTION);
        prevIntent.putExtra(Constants.widgetID, appWidgetId);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, prevIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.prev, prevPendingIntent);

        Intent nextIntent = new Intent(context, WidgetReceiver.class);
        nextIntent.setAction(Constants.NEXT_ACTION);
        nextIntent.putExtra(Constants.widgetID, appWidgetId);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, nextIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.next, nextPendingIntent);

        Intent repeatIntent = new Intent(context, WidgetReceiver.class);
        repeatIntent.putExtra(Constants.widgetID, appWidgetId);
        repeatIntent.setAction(Constants.REPEAT_ACTION);
        PendingIntent repeatPendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, repeatIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.repeat, repeatPendingIntent);

        Intent artIntent = new Intent(context, WidgetReceiver.class);
        artIntent.putExtra(Constants.widgetID, appWidgetId);
        artIntent.setAction(Constants.ART_CLICKED_ACTION);
        PendingIntent artPendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, artIntent, INTENT_FLAGS);
        views.setOnClickPendingIntent(R.id.songArt, artPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent updateAll = new Intent(context, WidgetReceiver.class);
        updateAll.setAction(Constants.UPDATE_ALL_ACTION);
        context.sendBroadcast(updateAll );
//        views.setOnClickPendingIntent(R.id.songArt, updateAllPendingIntent);
//        appWidgetManager.updateAppWidget(appWidgetIds, views);

//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {

        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


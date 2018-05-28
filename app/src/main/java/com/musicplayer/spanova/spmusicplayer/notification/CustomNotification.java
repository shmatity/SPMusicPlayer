package com.musicplayer.spanova.spmusicplayer.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.media.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomNotification extends Notification {
    private Context ctx;
    private NotificationManager mNotificationManager;
    boolean isPlaying = false;
    Notification notification;

    private static final CustomNotification instance = new CustomNotification();

    private CustomNotification(){}

    public static CustomNotification getInstance(){
        return instance;
    }

    public void updateNotification (Context ctx, String title, String message, int icon, Bitmap image, boolean isPlaying) {
        this.ctx = ctx;
        this.isPlaying = isPlaying;
        String ns = Context.NOTIFICATION_SERVICE;
        this.mNotificationManager = (NotificationManager) ctx.getSystemService(ns);

        Intent intent = new Intent(ctx, MusicPlayerActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, (int)System.currentTimeMillis(), intent, 0);

        Builder builder = new Builder(ctx);
        builder.setContentText(title);
        builder.setContentIntent(pIntent);
        builder.setContentTitle(message);
        builder.setSmallIcon(icon);
        builder.setLargeIcon(image);
        builder.setOngoing(true);
        setActions(builder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setStyle(new MediaStyle().setShowActionsInCompactView(2, 0, 1));
        }
        builder.setPriority(Notification.PRIORITY_MAX);
        notification = builder.build();
        try {
            mNotificationManager.notify(Constants.notificationID, notification);
        } catch (Exception e) {
            Log.d("Sp", e.getMessage());
        }
    }

    public void setActions( Notification.Builder builder) {
        Intent playIntent = new Intent("PLAY");
        Intent nextIntent = new Intent("NEXT");
        Intent prevIntent = new Intent("PREV");
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(ctx, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(ctx, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(ctx, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int playIcon = R.drawable.ic_play_black;
        if(isPlaying) playIcon = R.drawable.ic_pause_black;
        builder.addAction(playIcon, "Play", playPendingIntent); // #0
        builder.addAction(R.drawable.ic_next_black, "Next", nextPendingIntent);  // #1
        builder.addAction(R.drawable.ic_previous_black, "Prev", prevPendingIntent);  // #2
    }
}
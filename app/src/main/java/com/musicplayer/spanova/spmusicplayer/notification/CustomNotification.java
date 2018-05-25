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
import android.support.v4.media.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomNotification extends Notification {

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public CustomNotification(Context ctx, Activity activity, MediaPlayer player, String title, String message, int icon, Bitmap image){
        super();
        this.ctx = ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);
        builder.setContentText(title);
        builder.setContentTitle(message);
        builder.setSmallIcon(icon);
        builder.setLargeIcon(image);
        builder.setOngoing(true);
        setActions(activity, player, builder);
        builder.setStyle(new Notification.MediaStyle().setShowActionsInCompactView(2, 0, 1));
        builder.setPriority(Notification.PRIORITY_MAX);

        try {
            mNotificationManager.notify(548853, builder.build());
        } catch (Exception e) {
            Log.d("Sp", e.getMessage());
        }
    }

    public void setActions(Activity activity, MediaPlayer player, Notification.Builder builder) {
        Intent playIntent = new Intent("PLAY");
        Intent nextIntent = new Intent("NEXT");
        Intent prevIntent = new Intent("PREV");
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(ctx, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(ctx, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(ctx, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent playPendingIntent = PendingIntent.getService(ctx, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent nextPendingIntent = PendingIntent.getService(ctx, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent prevPendingIntent = PendingIntent.getService(ctx, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.addAction(R.drawable.ic_play, "Play", playPendingIntent); // #0
//        builder.addAction(R.drawable.ic_next, "Next", nextPendingIntent);  // #1
//        builder.addAction(R.drawable.ic_prev, "Prev", prevPendingIntent);  // #2
    }
}
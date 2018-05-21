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
    public CustomNotification(Context ctx, Activity activity, String title, String message, int icon, Bitmap image){
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
        builder.setAutoCancel(true);

        PendingIntent playPendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, NotificationPlayPauseActivity.class), 0);
        PendingIntent stopPendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, NotificationStopActivity.class), 0);
        builder.addAction(R.drawable.ic_play, "Previous", playPendingIntent); // #0
        builder.addAction(R.drawable.ic_stop, "Pause", stopPendingIntent);  // #1

        builder.setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0,1));


//
//        @SuppressWarnings("deprecation")
//        Notification notification = builder.getNotification();
//        notification.when = when;
//        notification.tickerText = title;
//        notification.icon = icon;
//        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.notification);
//        NotificationPlayPauseActivity playPauseActivity = new NotificationPlayPauseActivity();

//        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.notification, null);
//        final TextView textViewToChange = contentView.findViewById(R.id.message);
//        textViewToChange.setText(title);

//        notification.contentView = contentView;
//        notification.flags |= CustomNotification.FLAG_ONGOING_EVENT;
//        NotificationManager mNotificationManager =
//                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(548853, notification);
//
//        Intent play = new Intent(ctx, NotificationHelperActivity.class);
//        play.putExtra("DO", "play");
//        PendingIntent pPlay = PendingIntent.getActivity(ctx, 0, play, 0);
//        contentView. (R.id.play, pPlay);
//



        //set the button listeners
       // setListeners(contentView);


        try {
            mNotificationManager.notify(548853, builder.build());
        } catch (Exception e) {
            Log.d("Sp", e.getMessage());
        }
    }

    public void setListeners(RemoteViews view){
        //radio listener
        Intent play = new Intent(ctx, NotificationHelperActivity.class);
        play.putExtra("DO", "play");
        PendingIntent pPlay = PendingIntent.getActivity(ctx, 0, play, 0);
        view.setOnClickPendingIntent(R.id.play, pPlay);

        Intent stop = new Intent(ctx, NotificationHelperActivity.class);
        stop.putExtra("DO", "stop");
        PendingIntent pStop = PendingIntent.getActivity(ctx, 4, stop, 0);
        view.setOnClickPendingIntent(R.id.stop, pStop);
    }

}
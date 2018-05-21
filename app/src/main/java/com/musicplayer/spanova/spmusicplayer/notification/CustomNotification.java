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
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.R;

public class CustomNotification extends Notification {

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public CustomNotification(Context ctx, String title, String message, int icon, Bitmap image){
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
//        Intent detailsIntent = new Intent(ctx.getApplicationContext(),
//                NotificationPlayPauseActivity.class);
//        builder.addAction(new Action(R.drawable.ic_format_list_bulleted_black_24dp, "",
//                detailsIntent))

//        @SuppressWarnings("deprecation")
//        Notification notification = builder.getNotification();
//        notification.when = when;
//        notification.tickerText = title;
//        notification.icon = icon;
//        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.notification);
////        final TextView textViewToChange = ctx.getApplicationContext().findViewById(R.id.message);
////        textViewToChange.setText(title);
//
//        notification.contentView = contentView;
//        notification.flags |= CustomNotification.FLAG_ONGOING_EVENT;
//        NotificationManager mNotificationManager =
//                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        mNotificationManager.notify(548853, notification);


//



        //set the button listeners
       // setListeners(contentView);


        try {
            mNotificationManager.notify(548853, builder.build());
        } catch (Exception e) {
            Log.d("Sp", e.getMessage());
        }
    }

//    public void setListeners(RemoteViews view){
//        //radio listener
//        Intent radio=new Intent(ctx,NotificationHelperActivity.class);
//        radio.putExtra("DO", "radio");
//        PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, radio, 0);
//        view.setOnClickPendingIntent(R.id.radio, pRadio);
//
////        //volume listener
////        Intent volume=new Intent(ctx, NotificationHelperActivity.class);
////        volume.putExtra("DO", "volume");
////        PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, volume, 0);
////        view.setOnClickPendingIntent(R.id.volume, pVolume);
//
////        //reboot listener
////        Intent reboot=new Intent(ctx, NotificationHelperActivity.class);
////        reboot.putExtra("DO", "reboot");
////        PendingIntent pReboot = PendingIntent.getActivity(ctx, 5, reboot, 0);
////        view.setOnClickPendingIntent(R.id.reboot, pReboot);
//
//        //top listener
//        Intent top=new Intent(ctx, NotificationHelperActivity.class);
//        top.putExtra("DO", "top");
//        PendingIntent pTop = PendingIntent.getActivity(ctx, 3, top, 0);
//        view.setOnClickPendingIntent(R.id.top, pTop);
//
//        //app listener
//        Intent app=new Intent(ctx, NotificationHelperActivity.class);
//        app.putExtra("DO", "app");
//        PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
//        view.setOnClickPendingIntent(R.id.btn1, pApp);
//    }

}
package com.musicplayer.spanova.spmusicplayer.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.musicplayer.spanova.spmusicplayer.R;

public class NotificationPanel {

    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;

    public NotificationPanel(Context parent) {
        this.parent = parent;
        nBuilder = new NotificationCompat.Builder(parent)
                .setContentTitle("Parking Meter")
                .setSmallIcon(R.drawable.ic_format_list_bulleted_black_24dp)
                .setOngoing(true);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notification);

        //set the button listeners
//        setListeners(remoteView);
        nBuilder.setContent(remoteView);

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(2, nBuilder.build());
    }

//    public void setListeners(RemoteViews view){
//        //listener 1
//        Intent volume = new Intent(parent,NotificationReturnSlot.class);
//        volume.putExtra("DO", "volume");
//        PendingIntent btn1 = PendingIntent.getActivity(parent, 0, volume, 0);
//        view.setOnClickPendingIntent(R.id.btn1, btn1);
//
//        //listener 2
//        Intent stop = new Intent(parent, NotificationReturnSlot.class);
//        stop.putExtra("DO", "stop");
//        PendingIntent btn2 = PendingIntent.getActivity(parent, 1, stop, 0);
//        view.setOnClickPendingIntent(R.id.btn2, btn2);
//    }

    public void notificationCancel() {
        nManager.cancel(2);
    }
}
package com.musicplayer.spanova.spmusicplayer.utils;

import android.appwidget.AppWidgetManager;
import android.provider.MediaStore;

import com.musicplayer.spanova.spmusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static int notificationID = 548853;
    public static String widgetID = "WIDGET_ID";
    public static final int RUNTIME_PERMISSION_CODE = 7;
    public static String shouldContinueExtra = "shouldContinue";
    //ACTIONS
    public static String ACTION = "ACTION";
    public static String PLAY_PAUSE_ACTION = "PLAY_PAUSE";
    public static String PREV_ACTION = "PREV";
    public static String NEXT_ACTION = "NEXT";
    public static String SHUFFLE_ACTION = "SHUFFLE";
    public static String REPEAT_ACTION = "REPEAT";
    public static String ART_CLICKED_ACTION = "ART_CLICKED";
    public static String UPDATE_ALL_ACTION = "UPDATE_ALL";
    public static String STOP_ACTION = "STOP";

    //WIDGET ACTIONS
    public static String WIDGET_TAG = "_WIDGET";
    public static String WIDGET_UPDATE_ALL_ACTION = "STOP_WIDGET";
    public static String WIDGET_PLAY_PAUSE_ACTION = "PLAY_PAUSE_WIDGET";
    public static String WIDGET_PREV_ACTION = "PREV_WIDGET";
    public static String WIDGET_NEXT_ACTION = "NEXT_WIDGET";
    public static String WIDGET_SHUFFLE_ACTION = "SHUFFLE_WIDGET";
    public static String WIDGET_REPEAT_ACTION = "REPEAT_WIDGET";

    //NOTIFICATION ACTIONS
    public static String NOTIFICATION_TAG = "_NOTIFICATION";
    public static String NOTIFICATION_STOP_ACTION = "STOP_NOTIFICATION";
    public static String NOTIFICATION_PLAY_PAUSE_ACTION = "PLAY_PAUSE_NOTIFICATION";
    public static String NOTIFICATION_PREV_ACTION = "PREV_NOTIFICATION";
    public static String NOTIFICATION_NEXT_ACTION = "NEXT_NOTIFICATION";

    public static int REPEAT_NONE = 0;
    public static int REPEAT_ALL = 1;
    public static int REPEAT_SINGLE = 2;

    public static SortOption[] sortOptions = new SortOption[]{
            new SortOption(0, "Title", MediaStore.Audio.Media.TITLE + " ASC"),
            new SortOption(1, "Artist", MediaStore.Audio.Media.ARTIST + " ASC"),
            new SortOption(2, "Album", MediaStore.Audio.Media.ALBUM + " ASC"),
            new SortOption(3, "Newer", MediaStore.Audio.Media.DATE_ADDED + " DESC"),
            new SortOption(4, "Older", MediaStore.Audio.Media.DATE_ADDED + " ASC"),

    };

    public enum ListTypes {
        SONG(0),
        ARTIST(1),
        ALBUM(2),
        FOLDER(3);

        private int index;

        ListTypes(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}

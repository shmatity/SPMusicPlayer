package com.musicplayer.spanova.spmusicplayer.utils;

import android.appwidget.AppWidgetManager;
import android.provider.MediaStore;

import com.musicplayer.spanova.spmusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static int notificationID = 548853;
    public static String ACTION = "ACTION";
    public static String PLAY_PAUSE_ACTION = "PLAY_PAUSE";
    public static String PREV_ACTION = "PREV";
    public static String NEXT_ACTION = "NEXT";
    public static String SHUFFLE_ACTION = "SHUFFLE";
    public static String REPEAT_ACTION = "REPEAT";
    public static String ART_CLICKED_ACTION = "ART_CLICKED";
    public static String widgetID = "WIDGET_ID";//AppWidgetManager.EXTRA_APPWIDGET_ID;;

    public enum REPEAT {
        NONE(R.drawable.ic_repeat_gray, 0),
        ALL(R.drawable.ic_repeat_black, 1),
        SINGLE(R.drawable.ic_repeat_one_black, 2);

        public int getDrowable() {
            return drowable;
        }

        public void setDrowable(int drowable) {
            this.drowable = drowable;
        }

        private int drowable;
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        REPEAT(int drowable, int index) {
            this.drowable = drowable;
            this.index = index;
        }
    };

    public static SortOption[] sortOptions = new SortOption[] {
            new SortOption(0, "Title", MediaStore.Audio.Media.TITLE + " ASC"),
            new SortOption(1, "Artist", MediaStore.Audio.Media.ARTIST + " ASC"),
            new SortOption(2,"Album", MediaStore.Audio.Media.ALBUM + " ASC"),
            new SortOption(3,"Newer", MediaStore.Audio.Media.DATE_ADDED + " DESC"),
            new SortOption(4,"Older", MediaStore.Audio.Media.DATE_ADDED + " ASC"),

    };

    public static String[] generateSortSpinnerOptions() {
        List<String> listResult = new ArrayList<String>();
        for ( SortOption option : sortOptions) {
            listResult.add(option.getTitle());
        }
        String[] result = new String[listResult.size()];
        return listResult.toArray(result);
    }
}

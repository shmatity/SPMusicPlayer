package com.musicplayer.spanova.spmusicplayer.utils;

import com.musicplayer.spanova.spmusicplayer.R;

public class Constants {
    public static int notificationID = 548853;
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
    }
}

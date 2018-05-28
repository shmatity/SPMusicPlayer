package com.musicplayer.spanova.spmusicplayer.utils;

public class SortOption {
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderString() {
        return orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    int index;
    String title;
    String orderString;

    public SortOption(int index, String title, String orderString) {
        this.index = index;
        this.title = title;
        this.orderString = orderString;
    }


}

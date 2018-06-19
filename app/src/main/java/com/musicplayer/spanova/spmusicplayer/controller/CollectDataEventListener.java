package com.musicplayer.spanova.spmusicplayer.controller;

import android.content.Context;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;

public interface CollectDataEventListener {
    void run(Context context, MusicService ms);
}

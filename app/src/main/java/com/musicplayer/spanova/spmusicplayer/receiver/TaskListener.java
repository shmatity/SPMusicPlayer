package com.musicplayer.spanova.spmusicplayer.receiver;

import android.content.Context;

import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;

public interface TaskListener{
    void run(Context context, MusicService ms);
}

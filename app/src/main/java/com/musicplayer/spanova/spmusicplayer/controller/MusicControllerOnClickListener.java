package com.musicplayer.spanova.spmusicplayer.controller;

import android.view.View;

import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;

interface MusicControllerOnClickListener extends View.OnClickListener{
    void onClick(View v, MusicService musicService);
}

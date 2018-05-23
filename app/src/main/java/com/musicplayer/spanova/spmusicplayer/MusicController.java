package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.view.View;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c, boolean b){
        super(c, b);
    }

    public void hide( ){
    }

    public void hide(boolean hide){
        if(hide) this.hide();
    }

}
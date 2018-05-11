package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    Context context;
    String uri;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.setUri(uri);
            musicBound = true;
            songPicked();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        context = getApplicationContext();
        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");

        EditText editText = (EditText) findViewById(R.id.test);
        editText.setText(uri);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void songPicked(){
        musicSrv.setUri(uri);
        musicSrv.playSong();
    }
}

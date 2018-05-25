package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.song.Song;

public class MusicPlayerActivity extends AppCompatActivity {

    MusicService musicSrv;
    MediaPlayer player;
    Intent playIntent;
    boolean musicBound=false;
    Handler handler;
    int mCurrentBufferPercentage;
    SeekBar seekBar;
    TextView currentSec;
    TextView maxSec;
    Runnable runnable;
    Context context;
    Song song;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            player = musicSrv.getPlayer();
            musicBound = true;
            playSong();
            musicSrv.playSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            player = null;
            musicBound = false;
            if(handler!=null){
                handler.removeCallbacks(runnable);
            }
        }
    };

    public void showNotification(){
        new CustomNotification(context,
                this,
                player,
                song.getArtist(),
                song.getTitle(),
                R.drawable.ic_format_list_bulleted_black_24dp,
                song.getImageFromSong(song.getUri(), getResources()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        context = getApplicationContext();
        mCurrentBufferPercentage = 0;
        handler = new Handler();

        ImageButton shuffleButton = (ImageButton) findViewById(R.id.shuffle);
        shuffleButton.setBackgroundResource(R.drawable.ic_shuffle_gray);
        shuffleButton.setOnClickListener(shuffleListener);

        ImageButton prevButton = (ImageButton) findViewById(R.id.prev);
        prevButton.setOnClickListener(prevListener);

        ImageButton playPauseButton = (ImageButton) findViewById(R.id.playPause);
        playPauseButton.setBackgroundResource(R.drawable.ic_play_black);
        playPauseButton.setOnClickListener(playPauseListener);

        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(nextListener);

        ImageButton repeatButton = (ImageButton) findViewById(R.id.repeat);
        repeatButton.setBackgroundResource(R.drawable.ic_repeat_gray);
        repeatButton.setOnClickListener(repeatListener);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        currentSec = (TextView) findViewById(R.id.currentSec);

        maxSec = (TextView) findViewById(R.id.maxSec);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Toast.makeText(context,"onPointerCaptureChanged",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(context,"onActivity touch",Toast.LENGTH_SHORT).show();
        return false;
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            if(player != null){
                player.seekTo(i*1000);
            }
//            if(player!=null)
//            {
//                int mpos = player.getCurrentPosition();
//                int mdur= player.getDuration();
//
//                seekBar.setProgress(player.getCurrentPosition());
//                currentSec.setText(String.valueOf((float)mpos/100) + " s ");
//                maxSec.setText(String.valueOf((float)mdur/100)+ "m");
//            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    View.OnClickListener playPauseListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(musicSrv.isPlaying()) {
                musicSrv.pause();
                v.setBackgroundResource(R.drawable.ic_pause_black);
            } else {
                playSong();
                v.setBackgroundResource(R.drawable.ic_play_black);
            }
        }
    };

    View.OnClickListener shuffleListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            musicSrv.setShuffle();
            if(musicSrv.getShuffle()) {
                v.setBackgroundResource(R.drawable.ic_shuffle_black);
            } else {
                v.setBackgroundResource(R.drawable.ic_shuffle_gray);
            }
        }
    };

    View.OnClickListener prevListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            musicSrv.playPrev();
        }
    };

    View.OnClickListener nextListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            musicSrv.playNext();
        }
    };

    View.OnClickListener repeatListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            musicSrv.setRepeat();
            v.setBackgroundResource(musicSrv.getRepeatImage());
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
        new MediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
        }
    };

    protected void initializeSeekBar(){
        seekBar.setMax(player.getDuration()/1000);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(player!=null){
                    int mCurrentPosition = player.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                handler.postDelayed(runnable,1000);
            }
        };
    }

    protected void getAudioStats(){
        int duration  = player.getDuration()/1000; // In milliseconds
        int due = (player.getDuration() - player.getCurrentPosition())/1000;
        int pass = duration - due;

//        seekBar.setMax(player.getDuration());
        maxSec.setText(String.valueOf((float)player.getDuration()/100)+ "m");
        currentSec.setText(String.valueOf((float)player.getCurrentPosition()/100) + " s ");
    }

    private void playSong() {
        musicSrv.start();
        initializeSeekBar();
        seekBar.setProgress(player.getCurrentPosition());
        seekBar.setMax(player.getDuration());
//        maxSec.setText(String.valueOf((float)player.getDuration()/100)+ "m");
//        currentSec.setText(String.valueOf((float)player.getCurrentPosition()/100) + " s ");
    }
}

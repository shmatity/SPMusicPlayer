package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    MusicService musicSrv;
    Intent playIntent;
    boolean musicBound=false;
    int mCurrentBufferPercentage;
    Handler handler;
    Runnable runnable;
    Context context;
    Song song;

    SeekBar seekBar;
    TextView currentSec;
    TextView maxSec;
    TextView songTitle;
    TextView artistName;
    TextView albumName;
    ImageButton shuffleButton ;
    ImageButton prevButton ;
    ImageButton playPauseButton;
    ImageButton nextButton ;
    ImageButton repeatButton;
    ImageView songImage;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicBound = true;
            musicSrv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax((int) (musicSrv.getDuration()/1000));
                    initializeSeekBar();
                }
            });

            setContentView(R.layout.activity_music_player);

            seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
            currentSec = (TextView) findViewById(R.id.currentSec);
            maxSec = (TextView) findViewById(R.id.maxSec);

            initializControls();
            initializeSongInfo();
            musicSrv.playSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;

        }
    };

//
//    public void showNotification(){
//        new CustomNotification(context,
//                this,
//                player,
//                song.getArtist(),
//                song.getTitle(),
//                R.drawable.ic_format_list_bulleted_black_24dp,
//                song.getImageFromSong(song.getUri(), getResources()));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        mCurrentBufferPercentage = 0;
        handler = new Handler();
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if( playIntent == null ){
//            playIntent = new Intent(context, MusicService.class);
//            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            startService(playIntent);
//        }
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
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(musicSrv != null && fromUser){
                musicSrv.seekTo(progress * 1000);
            }
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
                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_black));
            } else if(musicSrv.isPaused()){
                musicSrv.start();
            } else {
                musicSrv.playSong();
                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_black));
            }
        }
    };

    View.OnClickListener shuffleListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            musicSrv.setShuffle();
            if(musicSrv.getShuffle()) {
                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shuffle_black));
            } else {
                ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shuffle_gray));
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
            ((ImageButton) v).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), musicSrv.getRepeatImage()));
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
        new MediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
        }
    };
    protected void initializControls() {

        shuffleButton = (ImageButton) findViewById(R.id.shuffle);
        shuffleButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shuffle_gray));
        shuffleButton.setOnClickListener(shuffleListener);

        prevButton = (ImageButton) findViewById(R.id.prev);
        prevButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_previous_black));
        prevButton.setOnClickListener(prevListener);

        playPauseButton = (ImageButton) findViewById(R.id.playPause);
        playPauseButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_black));
        playPauseButton.setOnClickListener(playPauseListener);

        nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_next_black));
        nextButton.setOnClickListener(nextListener);

        repeatButton = (ImageButton) findViewById(R.id.repeat);
        repeatButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_repeat_gray));
        repeatButton.setOnClickListener(repeatListener);
    }

    protected void initializeSongInfo() {
        songTitle = (TextView) findViewById(R.id.songTitle);
        artistName = (TextView) findViewById(R.id.artistName);
        albumName = (TextView) findViewById(R.id.albumName);
        songImage = (ImageView) findViewById(R.id.songImage);

        songTitle.setText(musicSrv.getSong().getTitle());
        artistName.setText(musicSrv.getSong().getArtist());
        albumName.setText(musicSrv.getSong().getAlbum());
        songImage.setImageBitmap(musicSrv.getSong().getImageFromSong(musicSrv.getSong().getUri(), getApplicationContext()));
    }

    protected void initializeSeekBar() {
        setMaxSec();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(musicSrv != null){
                    seekBar.setProgress((int) (musicSrv.getCurrentPosition() / 1000) );
                    seekBar.getProgress();
                    setCurrentSec();
                }
                handler.postDelayed(runnable,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }

    protected void  setMaxSec() {
        int duration  = musicSrv.getDuration(); // In milliseconds
        String durationString = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        maxSec.setText(durationString);
    }

    protected void setCurrentSec(){
        int pass =  musicSrv.getCurrentPosition();
        String passString = String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes(pass),
                TimeUnit.MILLISECONDS.toSeconds(pass) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pass))
        );
        currentSec.setText(passString);
    }

}

package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.view.View;

import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.notification.NotificationPanel;
import com.musicplayer.spanova.spmusicplayer.song.Song;

public class MusicPlayerActivity extends AppCompatActivity implements  MediaController.MediaPlayerControl {

    private MusicController controller;
    private MusicService musicSrv;
    private MediaPlayer player;
    private Intent playIntent;
    private boolean musicBound=false;
    private int mCurrentBufferPercentage;
    Context context;
    Song song;



    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            player = musicSrv.getPlayer();
            musicSrv.setUri(song.getUri());
            musicBound = true;
            songPicked();
            controller.show();
            showNotification();
            //finish();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controller.hide();
            player = null;
            musicBound = false;
        }
    };

    public void showNotification(){
        new CustomNotification(context,
                song.getArtist(),
                song.getTitle(),
                R.drawable.ic_format_list_bulleted_black_24dp,
                song.getImageFromSong(song.getUri(), getResources()));
//        NotificationPanel nPanel = new NotificationPanel(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        context = getApplicationContext();
        Intent intent = getIntent();
        song = (Song) intent.getSerializableExtra("Song");
        mCurrentBufferPercentage = 0;
        controller = new MusicController(this);
        controller.setPrevNextListeners(onPrev, onNext);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            controller.setAnchorView(findViewById(R.id.activity_music_player));
            controller.setMediaPlayer(this);
            controller.setEnabled(true);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d("dfsg","asdfasd");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        if (controller.isShowing()) {
            controller.hide();
            return true;
        } else {
            controller.show();
            return true;
        }
    }

    @Override
    public void start() {
        if(player != null) {
            player.start();
        }
    }

    @Override
    public void pause() {
        if(player != null) {
            player.pause();
        }
    }

    @Override
    public int getDuration() {
        if(player != null) {
            return player.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if(player != null) {
            player.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if(player != null) {
            return player.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return mCurrentBufferPercentage;
    }

    @Override
    public boolean canPause() {
        if(player != null) {
            return player.isPlaying();
        }
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        if (player != null) {
            return player.getAudioSessionId();
        }
        return 0;
    }

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

    private View.OnClickListener onPrev =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("SP", "prev");
                }
            };

    private View.OnClickListener onNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("SP", "next");
        }
    };

    public void songPicked() {
        musicSrv.setUri(song.getUri());
        musicSrv.playSong();
        controller.show();
    }

    private void toggleMediaControlsVisiblity() {
        if (controller.isShowing()) {
            controller.hide();
        } else {
            controller.show();
        }
    }

}

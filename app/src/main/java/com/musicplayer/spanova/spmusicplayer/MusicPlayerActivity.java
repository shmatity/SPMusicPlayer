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
import android.widget.MediaController;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.song.Song;

public class MusicPlayerActivity extends AppCompatActivity implements  MediaController.MediaPlayerControl {

    private MusicController controller;
    private MusicService musicSrv;
    private MediaPlayer player;
    private Intent playIntent;
    private boolean musicBound=false;
    private Handler handler = new Handler();
    private int mCurrentBufferPercentage;
    Context context;
    Song song;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            player = musicSrv.getPlayer();
            musicSrv.setSong(song);
            musicBound = true;
            songPicked();
            showNotification();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controller.hide(true);
            player = null;
            musicBound = false;
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
        Intent intent = getIntent();
        song = (Song) intent.getSerializableExtra("Song");
        song = (Song) intent.getSerializableExtra("Song");
        mCurrentBufferPercentage = 0;
        setController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            setController();
        }
    }


    public void setController () {
        controller = new MusicController(this, false);
        controller.setPrevNextListeners(onPrev, onNext);
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"onCLick",Toast.LENGTH_SHORT).show();
            }
        });
        controller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context,"OnTouch",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(getWindow().getDecorView().findViewById(R.id.activity_music_player));
        controller.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            controller.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View v) {
                    Toast.makeText(context,"OnTouch",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        controller.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Toast.makeText(context,"magic",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        controller.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Toast.makeText(context,"onViewAttachedToWindow",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Toast.makeText(context,"onViewDetachedFromWindow",Toast.LENGTH_SHORT).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            controller.setContextClickable(true);
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Toast.makeText(context,"onPointerCaptureChanged",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        Toast.makeText(context,"onActivity touch",Toast.LENGTH_SHORT).show();
        return false;
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
        musicSrv.setSong(song);
        musicSrv.playSong();
        controller.show(0);
        controller.requestFocus();
    }

    private void toggleMediaControlsVisiblity() {
        if (controller.isShowing()) {
            controller.hide();
        } else {
            controller.show();
        }
    }

    private void playNext(){
        musicSrv.playNext();
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        controller.show(0);
    }

}

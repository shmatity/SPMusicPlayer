package com.musicplayer.spanova.spmusicplayer.song;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.MainActivity;
import com.musicplayer.spanova.spmusicplayer.MusicController;
import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.song.SongAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment implements  MediaController.MediaPlayerControl {

    Activity activity;
    ListView listView;
    List<Song> ListElementsArrayList;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;

    private MusicController controller;
    private MusicService musicSrv;
    private MediaPlayer player;
    private Intent playIntent;
    private boolean musicBound=false;
    private Handler handler = new Handler();
    private int mCurrentBufferPercentage;
    Context context;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            player = musicSrv.getPlayer();
            musicBound = true;
            ListElementsArrayList = GetAllMediaMp3Files();
            musicSrv.setSongList(ListElementsArrayList);
            SongAdapter adapter = new SongAdapter(activity, ListElementsArrayList);
            listView.setAdapter(adapter);
            setController();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            controller.hide(true);
            player = null;
            musicBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = this.getActivity();
        context = activity.getApplicationContext();
        mCurrentBufferPercentage = 0;
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            activity.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            activity.startService(playIntent);
        }
        listView = (ListView) getView().findViewById(R.id.listView1);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setController();
                songPicked(position);
                showNotification();
            }
        });
    }

    public void setController () {
        controller = new MusicController(activity, false);
        controller.setMediaPlayer(this);
        controller.setAnchorView(activity.findViewById(R.id.activity_main));

//        controller.setAnchorView(activity.findViewById(R.id.activity_main));
        controller.setEnabled(true);


        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        },
        new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playNext();
                }
            });
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



    public List<Song> GetAllMediaMp3Files() {
        List<Song> result = new ArrayList<Song>();
        contentResolver = activity.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Toast.makeText(activity, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(activity, "No Music Found on SD Card.", Toast.LENGTH_LONG);

        } else {

            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int year = cursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
            int url = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                String songTitle = cursor.getString(title);
                String songArtist = cursor.getString(artist);
                String songAlbum = cursor.getString(album);
                int songYear = cursor.getInt(year);
                String songUri = cursor.getString(url);

                Song current =  new Song(id,
                                        songTitle,
                                        songArtist,
                                        "",
                                        songAlbum,
                                        songYear,
                                        songUri);
                result.add(current);

            } while (cursor.moveToNext());
        }
        return result;
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


    private void playNext(){
        musicSrv.playNext();
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        controller.show(0);
    }


    public void showNotification() {
        Song song = musicSrv.getSong();
        new CustomNotification(context,
                activity,
                player,
                song.getArtist(),
                song.getTitle(),
                R.drawable.ic_format_list_bulleted_black_24dp,
                song.getImageFromSong(song.getUri(), getResources()));
    }

    public void songPicked(int position) {
        musicSrv.setCurrentSongIndex(position);
        musicSrv.playSong();
        controller.show();
        controller.requestFocus();
    }
}
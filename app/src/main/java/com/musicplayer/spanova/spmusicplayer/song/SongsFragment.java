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

public class SongsFragment extends Fragment {

    Activity activity;
    ListView listView;
    List<Song> ListElementsArrayList;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;

    private MusicService musicSrv;
    private MediaPlayer player;
    private Intent playIntent;
    private boolean musicBound=false;
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
                songPicked(position);
            }
        });
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

    public void songPicked(int position) {
        musicSrv.setCurrentSongIndex(position);
        Intent myIntent = new Intent(context, MusicPlayerActivity.class);
        context.startActivity(myIntent);
    }
}
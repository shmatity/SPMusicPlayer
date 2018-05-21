package com.musicplayer.spanova.spmusicplayer.song;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.song.SongAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SongsFragment extends Fragment {


    Activity activity;

    Song[] ListElements = new Song[]{};

    ListView listView;

    List<Song> ListElementsArrayList;

    ArrayAdapter<Song> adapter;

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;

    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = this.getActivity();
        ListElementsArrayList = GetAllMediaMp3Files();
        listView = (ListView) getView().findViewById(R.id.listView1);
        SongAdapter adapter = new SongAdapter(activity, ListElementsArrayList);


        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Song current = ListElementsArrayList.get(position);

            Intent intent = new Intent(activity, MusicPlayerActivity.class);
            intent.putExtra("Song", current);
            startActivity(intent);


//        if (mediaPlayer != null) mediaPlayer.release();
//
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//
//        try {
//            Uri myUri = Uri.parse(uri);
//            mediaPlayer.setDataSource(context, myUri);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int url = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                String songTitle = cursor.getString(title);
                String songArtist = cursor.getString(artist);
                String songAlbum = cursor.getString(album);
                int songYear = cursor.getInt(year);
                String songUri = cursor.getString(url);



                Song current =  new Song(songTitle,
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
}
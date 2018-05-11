package com.musicplayer.spanova.spmusicplayer;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {


    Activity activity;



    Song[] ListElements = new Song[]{};

    ListView listView;

    List<Song> ListElementsArrayList;

    ArrayAdapter<Song> adapter;

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;

    Button button;
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_album, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = this.getActivity();
        ListElementsArrayList = GetAllMediaMp3Files();
        listView = (ListView) getView().findViewById(R.id.listView1);
        SongAdapter adapter = new SongAdapter(activity, ListElementsArrayList);


        listView.setAdapter(adapter);

        // ListView on item selected listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song current = ListElementsArrayList.get(position);
                Uri myUri = Uri.parse(current.uri);
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setDataSource(getApplicationContext(), myUri);
//                mediaPlayer.prepare();
                mediaPlayer.start();

                Toast.makeText(activity, parent.getAdapter().getItem(position).toString(), Toast.LENGTH_LONG).show();

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
            int uri = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                String songTitle = cursor.getString(title);
                String songArtist = cursor.getString(artist);
                String songAlbum = cursor.getString(album);
                String songYesr = cursor.getString(year);
                String songUri = cursor.getString(uri);

                // Adding Media File Names to ListElementsArrayList.
                Song current = new Song(songTitle, songArtist, songUri);
                result.add(current);

            } while (cursor.moveToNext());
        }
        return result;
    }
}
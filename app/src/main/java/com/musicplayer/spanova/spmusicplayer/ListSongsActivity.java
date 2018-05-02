package com.musicplayer.spanova.spmusicplayer;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Build;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListSongsActivity extends AppCompatActivity {

    Context context;

    public static final int RUNTIME_PERMISSION_CODE = 7;

    Song[] ListElements = new Song[]{};

    ListView listView;

    List<Song> ListElementsArrayList;

    ArrayAdapter<Song> adapter;

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        AndroidRuntimePermission();

        ListElementsArrayList = GetAllMediaMp3Files();
        listView = (ListView) findViewById(R.id.listView1);
        SongAdapter adapter = new SongAdapter(ListSongsActivity.this, ListElementsArrayList);


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

                Toast.makeText(ListSongsActivity.this, parent.getAdapter().getItem(position).toString(), Toast.LENGTH_LONG).show();

            }
        });

    }


    public List<Song> GetAllMediaMp3Files() {
        List<Song> result = new ArrayList<Song>();
        contentResolver = context.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Toast.makeText(ListSongsActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(ListSongsActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);

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

    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(ListSongsActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    ListSongsActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {

                    ActivityCompat.requestPermissions(
                            ListSongsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }
}
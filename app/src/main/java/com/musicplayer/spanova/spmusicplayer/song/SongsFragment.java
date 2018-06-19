package com.musicplayer.spanova.spmusicplayer.song;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.controller.CollectDataEventListener;
import com.musicplayer.spanova.spmusicplayer.controller.CpmpactMusicPlayer;
import com.musicplayer.spanova.spmusicplayer.controller.MusicController;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.utils.SortOption;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class SongsFragment extends Fragment {

    public CpmpactMusicPlayer controller;
    Activity activity;
    ListView listView;
    List<Song> ListElementsArrayList;
    String search = "*";
    int sortIndex = 0;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        activity = this.getActivity();
        context = activity.getApplicationContext();
        controller = new CpmpactMusicPlayer(context, activity, false);
        controller.setOnDataCollected(onDataCollected);
        controller.connect();
        return inflater.inflate(R.layout.fragment_songs, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (this.getArguments() != null) {
            search = getArguments().getString("search");
            sortIndex = getArguments().getInt("sort");
        }
        controller.setSort(sortIndex);
        controller.setSearch(search);
    }

    CollectDataEventListener onDataCollected = new CollectDataEventListener() {
        @Override
        public void run(Context context, MusicService ms) {
            ListElementsArrayList = controller.getSongList();
            SongAdapter adapter = new SongAdapter(activity, ListElementsArrayList);
            listView = (ListView) getView().findViewById(R.id.listView1);
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
                    controller.showForSongOnPosition(position);
                    //songPicked(position);
                }
            });
        }
    };

//    public void songPicked(int position) {
//        musicSrv.setCurrentSongIndex(position);
//        Intent myIntent = new Intent(context, MusicPlayerActivity.class);
//        context.startActivity(myIntent);
//    }
}
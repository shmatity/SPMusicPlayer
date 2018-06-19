package com.musicplayer.spanova.spmusicplayer.song;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.musicplayer.spanova.spmusicplayer.MusicPlayerActivity;
import com.musicplayer.spanova.spmusicplayer.sevice.MusicService;
import com.musicplayer.spanova.spmusicplayer.R;

import java.util.List;

public class SongsFragment extends Fragment {

    Activity activity;
    ListView listView;
    List<Song> ListElementsArrayList;
    String search = "*";
    int sortIndex = 0;
    private MusicService musicSrv;
    private Intent playIntent;
    Context context;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.setSortOption(sortIndex);
            musicSrv.setSearch(search);
            ListElementsArrayList = (List<Song>) musicSrv.getElementList();
            SongAdapter adapter = new SongAdapter(activity, ListElementsArrayList);
            listView.setAdapter(adapter);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
        if (this.getArguments() != null) {
            search = getArguments().getString("search");
            sortIndex = getArguments().getInt("sort");
        }
        if( playIntent == null ){
            playIntent = new Intent(context, MusicService.class);
            playIntent.setAction("A");
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

    public void songPicked(int position) {
        musicSrv.setCurrentElementIndex(position);
        Intent myIntent = new Intent(context, MusicPlayerActivity.class);
        context.startActivity(myIntent);
    }
}
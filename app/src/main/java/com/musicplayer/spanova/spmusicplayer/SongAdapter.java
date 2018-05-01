package com.musicplayer.spanova.spmusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class SongAdapter extends BaseAdapter {

    Context context;
    List<Song> songs;
    private static LayoutInflater inflater = null;

    public SongAdapter(Context context, List<Song> songs) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.songs = songs;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.song, null);
        TextView text = (TextView) vi.findViewById(R.id.title);
        text.setText(songs.get(position).title);
        TextView artist = (TextView) vi.findViewById(R.id.artist);
        artist.setText(songs.get(position).artist);
        return vi;
    }
}
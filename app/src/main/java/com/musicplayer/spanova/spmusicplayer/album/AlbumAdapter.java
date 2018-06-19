package com.musicplayer.spanova.spmusicplayer.album;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.artist.Artist;
import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {

    Context context;
    List<Album> album;
    private static LayoutInflater inflater = null;

    public AlbumAdapter(Context context, List<Album> album) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.album = album;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return album.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return album.get(position);
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
            vi = inflater.inflate(R.layout.album, null);
        TextView text = (TextView) vi.findViewById(R.id.name);
        text.setText(album.get(position).getName());
//        TextView count = (TextView) vi.findViewById(R.id.count);
//        count.setText(String.valueOf(album.get(position).getSongList().size()));
        return vi;
    }
}
package com.musicplayer.spanova.spmusicplayer.artist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.song.Song;

import java.util.List;

public class ArtistAdapter extends BaseAdapter {

    Context context;
    List<Artist> artists;
    private static LayoutInflater inflater = null;

    public ArtistAdapter(Context context, List<Artist> artists) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.artists = artists;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return artists.get(position);
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
            vi = inflater.inflate(R.layout.artist, null);
        TextView text = (TextView) vi.findViewById(R.id.name);
        text.setText(artists.get(position).name);
        TextView count = (TextView) vi.findViewById(R.id.count);
        count.setText(String.valueOf(artists.get(position).getSongList().size()));
        return vi;
    }
}
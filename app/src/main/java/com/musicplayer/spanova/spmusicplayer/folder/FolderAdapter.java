package com.musicplayer.spanova.spmusicplayer.folder;


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

public class FolderAdapter extends BaseAdapter {

    Context context;
    List<Folder> folders;
    private static LayoutInflater inflater = null;

    public FolderAdapter(Context context, List<Folder> folders) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.folders = folders;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return folders.get(position);
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
            vi = inflater.inflate(R.layout.folder, null);
        TextView text = (TextView) vi.findViewById(R.id.name);
        text.setText(folders.get(position).getName());
        TextView path = (TextView) vi.findViewById(R.id.path);
        path.setText(String.valueOf(folders.get(position).getPath()));
        return vi;
    }
}
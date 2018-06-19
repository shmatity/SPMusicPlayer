package com.musicplayer.spanova.spmusicplayer.sevice;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.album.Album;
import com.musicplayer.spanova.spmusicplayer.artist.Artist;
import com.musicplayer.spanova.spmusicplayer.folder.Folder;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.utils.SortOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaExtractor {
    ContentResolver contentResolver;
    Cursor cursor;
    String search = "";
    int sortIndex = 0;
    Context context;

    MediaExtractor(Context context) {
        this.context = context;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }


    public List<?> GetAllMediaMp3Files(Constants.ListTypes type) {
        SortOption sortOrder = Constants.sortOptions[sortIndex];
        String searchCriteria = null;
        String[] selectionArgs = null;

        if (search != null) {
            searchCriteria = MediaStore.Audio.Media.TITLE + " LIKE ? OR " +
                    MediaStore.Audio.Media.ARTIST + " LIKE ? OR " +
                    MediaStore.Audio.Media.ARTIST + " LIKE ? ";
            selectionArgs = new String[]{"%" + search + "%"};
        }

        switch (type) {
            case SONG:
                return getSongsByCriteria(searchCriteria,selectionArgs,sortOrder.getOrderString());
            case ARTIST:
                return getArtistByCriteria(searchCriteria,selectionArgs,sortOrder.getOrderString());
            case ALBUM:
                return getAlbumByCriteria(searchCriteria,selectionArgs,sortOrder.getOrderString());
            case FOLDER:
                return getFolderByCriteria(searchCriteria,selectionArgs,sortOrder.getOrderString());
            default:
                return getSongsByCriteria(searchCriteria,selectionArgs,sortOrder.getOrderString());
        }

    }

    public List<?> GetAllMediaMp3Files() {
       return GetAllMediaMp3Files(Constants.ListTypes.SONG);
    }

    public Song getSongByID(int id) {
        String searchCriteria = null;
        String[] selectionArgs = null;

        if (id > -1) {
            searchCriteria = MediaStore.Audio.Media._ID + " LIKE ?";
            selectionArgs = new String[]{"%" + id + "%"};
        }

        return getSongsByCriteria(searchCriteria,selectionArgs,null).get(0);
    }

    private List<Song> getSongsByCriteria (String searchCriteria, String[] selectionArgs,String sortOrder) {
        List<Song> result = new ArrayList<Song>();
        contentResolver = context.getContentResolver();
        cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                searchCriteria,
                selectionArgs,
                sortOrder
        );

        if (cursor == null) {

            Toast.makeText(context, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(context, "No Music Found on SD Card.", Toast.LENGTH_LONG);

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

                Song current = new Song(id,
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

    private List<Artist> getArtistByCriteria (String searchCriteria, String[] selectionArgs, String sortOrder) {
        List<Artist> result = new ArrayList<Artist>();
        HashMap<String, Artist> map = new HashMap();
        List<Song> songs = getSongsByCriteria("", null, "artist asc");
        String artist = songs.get(0).getArtist();

        List<Song> currentArtistSongList = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getArtist().equals(artist)) {
                currentArtistSongList.add(songs.get(i));
            } else {
                result.add(new Artist(artist, currentArtistSongList));
                artist = songs.get(i).getArtist();
                currentArtistSongList = new ArrayList<Song>();
                currentArtistSongList.add(songs.get(i));
            }
        }
        return result;
    }

    private List<Album> getAlbumByCriteria (String searchCriteria, String[] selectionArgs, String sortOrder) {
        List<Album> result = new ArrayList<Album>();
        List<Song> songs = getSongsByCriteria("", null, "album asc");
        String album = songs.get(0).getAlbum();

        List<Song> currentArtistSongList = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getAlbum().equals(album)) {
                currentArtistSongList.add(songs.get(i));
            } else {
                result.add(new Album(album, currentArtistSongList));
                album = songs.get(i).getAlbum();
                currentArtistSongList = new ArrayList<Song>();
                currentArtistSongList.add(songs.get(i));
            }
        }
        return result;
    }

    private List<Folder> getFolderByCriteria (String searchCriteria, String[] selectionArgs, String sortOrder) {
        List<Folder> result = new ArrayList<Folder>();
        List<Song> songs = getSongsByCriteria("", null, "_data asc");
        String temp = songs.get(0).getUri();
        String uri = temp.substring(0, temp.lastIndexOf('/'));

        List<Song> currentArtistSongList = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {

            if (songs.get(i).getUri().substring(0, songs.get(i).getUri().lastIndexOf('/')).equals(uri)) {
                currentArtistSongList.add(songs.get(i));
            } else {
                result.add(new Folder(uri, uri, currentArtistSongList));
                uri = songs.get(i).getAlbum();
                currentArtistSongList = new ArrayList<Song>();
                currentArtistSongList.add(songs.get(i));
            }
        }
        return result;
    }
}

package com.musicplayer.spanova.spmusicplayer.sevice;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

//import com.musicplayer.spanova.spmusicplayer.OnNextListener;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.utils.Utils;
import com.musicplayer.spanova.spmusicplayer.widget.WidgetReceiver;

import java.util.List;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    boolean shuffle = false;
    int repeat = Constants.REPEAT_NONE;
    Random rand;
    MediaPlayer player;
    int currentSongIndex = 0;
    private List<Song> ListElementsArrayList;
    final IBinder musicBind = new MusicBinder();
    MediaPlayer.OnPreparedListener onPreparedListener;
    MediaPlayer.OnCompletionListener onCompletionListener = null;
    MediaExtractor me;

    @Override
    public IBinder onBind(Intent arg0) {
        String data = Utils.readFromFile(getApplicationContext());
        me.getSongByUri(Utils.unpackUri(data));
        shuffle = Utils.unpackShuffle(data);
        repeat = Utils.unpackRepeat(data);
        setSongList(me.GetAllMediaMp3Files());
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.writeToFile(Utils.prepareData(getSong().getUri(), shuffle, repeat), getApplicationContext());
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (repeat == Constants.REPEAT_SINGLE) {
            playSong();
        } else if (repeat == Constants.REPEAT_ALL) {
            playNext();
        }
        if (onCompletionListener != null)
            this.onCompletionListener.onCompletion(mp);
        showNotification();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "SHIT HAPPEND! PLEASE CONTACT SOFKA!", Toast.LENGTH_LONG).show();
        Log.e("MUSIC SERVICE", "Error setting data source");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(Constants.notificationID, CustomNotification.getInstance());
        return START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this, "Bro! Delete something!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(onPreparedListener!= null) this.onPreparedListener.onPrepared(mp);
        mp.start();
        showNotification();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player != null) {
            player.stop();
            player.release();
        }
        initMusicPlayer();
        rand = new Random();
        me = new MediaExtractor(getApplicationContext());
//        String data = Utils.readFromFile(getApplicationContext());
//        me.getSongById(Utils.unpackID(data));
//        shuffle = Utils.unpackShuffle(data);
//        repeat = Utils.unpackRepeat(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.writeToFile(Utils.prepareData(getSong().getUri(), shuffle, repeat), getApplicationContext());
        player.stop();
        player.release();
        player = null;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void initMusicPlayer() {
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public List<Song> getSongList() {
        return ListElementsArrayList;
    }

    public void setSongList(List<Song> listElementsArrayList) {
        ListElementsArrayList = listElementsArrayList;
    }

    public void setSortOption(int sortOption) {
        if (me.getSortIndex() != sortOption) {
            me.setSortIndex(sortOption);
            setSongList(me.GetAllMediaMp3Files());
        }
    }

    public void setSearch(String search) {
        if (!me.getSearch().equals(search)) {
            me.setSearch(search);
            setSongList(me.GetAllMediaMp3Files());
        }
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }

    public boolean getShuffle() {
        return shuffle;
    }

    public void setRepeat() {
        if (repeat == Constants.REPEAT_NONE) {
            repeat = Constants.REPEAT_ALL;
        } else if (repeat == Constants.REPEAT_ALL) {
            repeat = Constants.REPEAT_SINGLE;
        } else if (repeat == Constants.REPEAT_SINGLE) {
            repeat = Constants.REPEAT_NONE;
        } else {
            repeat = Constants.REPEAT_NONE;
        }
    }

    public int getRepeat() {
        return repeat;
    }

    public Song getSong() {
        return this.ListElementsArrayList.get(currentSongIndex);
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public void setCurrentSongIndex(int currentSongIndex) {
        this.currentSongIndex = currentSongIndex;
    }

    public void playSong() {
        try {
            player.reset();
            player.setDataSource(getApplicationContext(), Uri.parse(getSong().getUri()));
            player.prepareAsync();
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void start() {
        player.start();
    }

    public void pause() {
        player.pause();
        showNotification();
    }

    public void stop() {
        player.stop();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    public Bitmap getSongImage() {
        return getSong().getImage();
    }

    public boolean isPaused() {
        return !player.isPlaying() && player.getCurrentPosition() > 1;
    }

    public void playPrev() {
        int currentIndex = 0;
        if (repeat == Constants.REPEAT_SINGLE) {
            playSong();
            return;
        }
        if (shuffle) {
            currentIndex = rand.nextInt(getSongList().size());
        } else {
            if (getCurrentSongIndex() > 0) {
                currentIndex = getCurrentSongIndex() - 1;
            }
        }
        setCurrentSongIndex(currentIndex);
        playSong();

    }

    public void playNext() {
        int currentIndex = 0;
        if (repeat == Constants.REPEAT_SINGLE) {
            playSong();
            return;
        }
        if (shuffle) {
            currentIndex = rand.nextInt(getSongList().size());
        } else {
            if (getSongList().size() > getCurrentSongIndex()) {
                currentIndex = getCurrentSongIndex() + 1;
            }
        }
        setCurrentSongIndex(currentIndex);
        playSong();
    }

    public void setDisplay(SurfaceHolder sf){
        player.setDisplay(sf);
    }

    public void prepareAsync() {
        player.prepareAsync();
    }

    public void showNotification() {
        Song song = getSong();
        CustomNotification.getInstance().updateNotification(this,
                song.getArtist(),
                song.getTitle(),
                R.drawable.ic_format_list_bulleted_black_24dp,
                song.getImageFromSong(this),
                player.isPlaying());


    }

}

// TBD
//https://medium.com/@deividi/a-good-way-to-handle-incoming-notifications-in-android-dc64c29041a5

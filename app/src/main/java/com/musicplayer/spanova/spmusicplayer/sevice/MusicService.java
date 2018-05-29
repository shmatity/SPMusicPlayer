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
import android.widget.Toast;

//import com.musicplayer.spanova.spmusicplayer.OnNextListener;
import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.notification.CustomNotification;
import com.musicplayer.spanova.spmusicplayer.song.Song;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.utils.Utils;

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

    private MediaPlayer player;
    private int currentSongIndex = 0;
    private List<Song> ListElementsArrayList;
    private final IBinder musicBind = new MusicBinder();
    MediaPlayer.OnPreparedListener onPreparedListener;
    MediaPlayer.OnCompletionListener onCompletionListener;
    MediaExtractor me;
//    OnNextListener onNextListener;
    private boolean shuffle = false;

    int repeat = Constants.REPEAT_NONE;
    private Random rand;

    @Override
    public IBinder onBind(Intent arg0) {
        setSongList(me.GetAllMediaMp3Files());
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        //showNotification();
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(repeat == Constants.REPEAT_SINGLE) {
            playSong();
        } else if(repeat == Constants.REPEAT_ALL) {
            playNext();
        }
        this.onCompletionListener.onCompletion(mp);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("MUSIC SERVICE", "Error setting data source");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        return START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(this,"Service LowMemory",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.onPreparedListener.onPrepared(mp);
        mp.start();
    }

    public void onCreate(){
        super.onCreate();
        if (player != null) {
            player.stop();
            player.release();
        }
        initMusicPlayer();
        rand = new Random();
        me = new MediaExtractor(getApplicationContext());
//        String data = Utils.readFromFile(getApplicationContext());
//        me.getMediaFileByID(Utils.unpackID(data));
//        shuffle = Utils.unpackShuffle(data);
//        repeat = Utils.unpackRepeat(data);
    }

    public void onDestroy(){
        super.onDestroy();
       // Utils.writeToFile(Utils.prepareData(getSong().getId(), shuffle, repeat), getApplicationContext());
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

//    public void setOnNextListener(OnNextListener listener) {
//        this.onNextListener = listener;
//    }

    public void initMusicPlayer(){
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
        if(me.getSortIndex() != sortOption) {
            me.setSortIndex(sortOption);setSongList(me.GetAllMediaMp3Files());
        }
    }

    public void setSearch(String search) {
        if(!me.getSearch().equals(search)) {
            me.setSearch(search);
            setSongList(me.GetAllMediaMp3Files());
        }
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

    public boolean getShuffle(){
        return shuffle;
    }

    public void setRepeat() {
        if(repeat == Constants.REPEAT_NONE) {
            repeat = Constants.REPEAT_ALL;
        } else if(repeat == Constants.REPEAT_ALL) {
            repeat = Constants.REPEAT_SINGLE;
        } else if(repeat == Constants.REPEAT_SINGLE) {
            repeat = Constants.REPEAT_NONE;
        } else {
            repeat = Constants.REPEAT_NONE;
        }
    }

    public  int getRepeatImage() {
        return Utils.getRepeatIcon(repeat);
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
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void playSongIsPaused() {
        if(player.isPlaying()) {
            pause();
        } else {
            playSong();
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void start(){
        player.start();
    }

    public void pause(){
        player.pause();
    }

    public void stop(){
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

    public void playPrev(){
        int currentIndex = 0;
        if(repeat == Constants.REPEAT_SINGLE) {
            playSong();
            return;
        }
        if(shuffle){
            currentIndex = rand.nextInt(getSongList().size());
        } else {
            if (getCurrentSongIndex() > 0) {
                currentIndex = getCurrentSongIndex() - 1;
            }
        }
        setCurrentSongIndex(currentIndex);
        playSong();

    }

    public void playNext(){
        int currentIndex = 0;
        if(repeat == Constants.REPEAT_SINGLE) {
            playSong();
            return;
        }
        if(shuffle){
            currentIndex = rand.nextInt(getSongList().size());
        } else {
            if (getSongList().size() > getCurrentSongIndex()) {
                currentIndex = getCurrentSongIndex() + 1;
            }
        }
        setCurrentSongIndex(currentIndex);
        playSong();
    }

    public void showNotification() {
        Song song = getSong();
        startForeground(Constants.notificationID,  CustomNotification.getInstance());
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

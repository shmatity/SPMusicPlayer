package com.musicplayer.spanova.spmusicplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.musicplayer.spanova.spmusicplayer.R;
import com.musicplayer.spanova.spmusicplayer.song.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static int getRepeatIcon(int i) {
        if(Constants.REPEAT_NONE == i) {
            return R.drawable.ic_repeat_gray;
        } else if(Constants.REPEAT_ALL == i) {
            return R.drawable.ic_repeat_black;
        } else if(Constants.REPEAT_SINGLE == i) {
            return R.drawable.ic_repeat_one_black;
        }
        return  R.drawable.ic_repeat_gray;
    }

    public static String[] generateSortSpinnerOptions() {
        List<String> listResult = new ArrayList<String>();
        for (SortOption option : Constants.sortOptions) {
            listResult.add(option.getTitle());
        }
        String[] result = new String[listResult.size()];
        return listResult.toArray(result);
    }

    public static String prepareData(int id, boolean shuffle, int repeat) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("shuffle", shuffle);
            json.put("repeat",repeat);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static int unpackID(String data) {
        JSONObject jObject = null;
        int id = 0;
        try {
            jObject = new JSONObject(data);
            id = jObject.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int unpackRepeat(String data) {
        JSONObject jObject = null;
        int repeat = 0;
        try {
            jObject = new JSONObject(data);
            repeat = jObject.getInt("repeat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repeat;
    }

    public static boolean unpackShuffle(String data) {
        JSONObject jObject = null;
        boolean shuffle = false;
        try {
            jObject = new JSONObject(data);
            shuffle = jObject.getBoolean("shuffle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shuffle;
    }

    public static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}

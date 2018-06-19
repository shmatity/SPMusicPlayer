package com.musicplayer.spanova.spmusicplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.musicplayer.spanova.spmusicplayer.album.AlbumFragment;
import com.musicplayer.spanova.spmusicplayer.artist.ArtistFragment;
import com.musicplayer.spanova.spmusicplayer.folder.FolderFragment;
import com.musicplayer.spanova.spmusicplayer.song.SongsFragment;
import com.musicplayer.spanova.spmusicplayer.utils.Constants;
import com.musicplayer.spanova.spmusicplayer.utils.SortOption;
import com.musicplayer.spanova.spmusicplayer.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;
    String searchedText = "";
    int sortOption = 0;
    int currentItem = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Spinner sortSpinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Utils.generateSortSpinnerOptions());
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            sortSpinner.setAdapter(adapter);

            sortSpinner.setOnItemSelectedListener(onSpinnerItemSelected);

            currentItem = item.getItemId();
            refresh();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ImageButton im = (ImageButton) findViewById(R.id.searchButton);
        im.setOnClickListener(searchButtonOnClickListener);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        context = getApplicationContext();
        AndroidRuntimePermission();
        ((BottomNavigationView) findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_songs);

    }

    public void refresh() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentItem == R.id.navigation_songs) {
            Bundle bundle = new Bundle();
            bundle.putString("search", searchedText);
            bundle.putInt("sort", sortOption);
            SongsFragment myObj = new SongsFragment();
            myObj.setArguments(bundle);
            ft.replace(R.id.fragment_view, myObj);
            ft.commit();
        } else if (currentItem == R.id.navigation_artist) {
            Bundle bundle = new Bundle();
            bundle.putString("search", searchedText);
            bundle.putInt("sort", sortOption);
            ArtistFragment myObj = new ArtistFragment();
            myObj.setArguments(bundle);
            ft.replace(R.id.fragment_view, myObj);
            ft.commit();
        } else if (currentItem == R.id.navigation_album) {
            Bundle bundle = new Bundle();
            bundle.putString("search", searchedText);
            bundle.putInt("sort", sortOption);
            AlbumFragment myObj = new AlbumFragment();
            myObj.setArguments(bundle);
            ft.replace(R.id.fragment_view, myObj);
            ft.commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("search", searchedText);
            bundle.putInt("sort", sortOption);
            FolderFragment myObj = new FolderFragment();
            myObj.setArguments(bundle);
            ft.replace(R.id.fragment_view, myObj);
            ft.commit();
        }
    }

    public void AndroidRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                    Constants.RUNTIME_PERMISSION_CODE);
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);
                    AlertDialog dialog = alert_builder.create();
                    dialog.show();

                } else {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.RUNTIME_PERMISSION_CODE);
                }
            } else {
                //GO BABY GO!
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case Constants.RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // <3
                } else {
                    Toast.makeText(context, "Are You Sure MF?", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    AdapterView.OnItemSelectedListener onSpinnerItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sortOption = position;
            ((TextView) view).setText("");
            refresh();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    TextView.OnEditorActionListener searchFieldOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            searchedText = v.getText().toString();
            return true;
        }
    };

    View.OnClickListener searchButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText searchEditText = (EditText) findViewById(R.id.searchEditText);
            searchEditText.setOnEditorActionListener(searchFieldOnEditorActionListener);
            if (searchEditText.getVisibility() == View.VISIBLE) {
                searchEditText.setVisibility(View.GONE);
                searchedText = searchEditText.getText().toString();
                refresh();
            } else {
                searchEditText.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };
}

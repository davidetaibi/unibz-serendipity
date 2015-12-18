package com.unibz.serendipity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ListenActivity extends Activity {
    private final String LOG_TAG = "LISTEN_ACTIVITY";
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_BACKGROUND = 200;

    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private Sound currentSound;
    private LinkedList<Sound> reachableSoundList;
    private BroadcastReceiver locationChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        mediaPlayer = null;
        currentSound = null;
        prepared = false;
        reachableSoundList = new LinkedList<>();
        //playButton = (ImageButton) findViewById(R.id.play_button);

        locationChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location newLocation = intent.getParcelableExtra(GPSTracker.EXTRA);
                if (newLocation != null) {
                    locationChanged(newLocation);
                }
            }
        };

        Location currentLocation = GPSTracker.getCurrentLocation();
        if (currentLocation != null) {
            locationChanged(currentLocation);
        }
    }

    public void locationChanged(Location location) {
        //Toast.makeText(this, "LocationChanged", Toast.LENGTH_LONG).show();
        //Log.d(LOG_TAG, "Location changed: lat: " + location.getLatitude() + "  long: " + location.getLongitude());

        reachableSoundList.clear();
        for (int i = 0; i < SoundList.soundList.size(); i++) {
            Sound sound = SoundList.soundList.get(i);
            if (sound.getDistance() <= DISTANCE_TO_BACKGROUND) {
                reachableSoundList.add(sound);
            }

            Collections.sort(reachableSoundList, new Comparator<Sound>() {
                @Override
                public int compare(Sound sound1, Sound sound2) {
                    return Double.compare(sound1.getDistance(), sound2.getDistance());
                }
            });
        }

        /*for (Sound sound : reachableSoundList) {
            Log.d(LOG_TAG, sound.getDistance() + "  " + sound.getTitle());
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(GPSTracker.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
    }

    /*public void clicked(View view) {
        switch (view.getId()) {
            case R.id.sound_play_pause_button:
                playSound();
                break;

        }
    }

    private void playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && prepared) {
            mediaPlayer.start();
        }
    }

    private void prepareSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            prepared = false;
        }
        if (currentSound != null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(LOG_TAG, "Sound " + currentSound.getTitle() + " prepared");
                    prepared = true;
                    playButton.setVisibility(View.VISIBLE);
                }
            });

            try {
                mediaPlayer.setDataSource(currentSound.getSoundLink());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error on setting data source");
                e.printStackTrace();
            }
        } else {
            playButton.setVisibility(View.GONE);
        }
    }*/

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }
}

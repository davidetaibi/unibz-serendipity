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
import java.util.LinkedList;

public class ListenActivity extends Activity {
    private final String LOG_TAG = "LISTEN_ACTIVITY";
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_BACKGROUND = 200;

    private GPSTracker gpsTracker;
    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private Sound currentSound;
    private LinkedList<Sound> backgroundSoundList = new LinkedList<Sound>();
    private ImageButton playButton;
    private BroadcastReceiver locationChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        mediaPlayer = null;
        currentSound = null;
        prepared = false;
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

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.play_button:
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
    }

    public void locationChanged(Location location) {
        Toast.makeText(this, "LocationChanged", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "Location changed: lat: " + location.getLatitude() + "  long: " + location.getLongitude());

        double distance = 0.0;
        for (int i = 0; i < SoundList.soundList.size(); i++) {
            Sound sound = SoundList.soundList.get(i);
            distance  = sound.getDistance(location);

            /*if (distance <= DISTANCE_TO_SOUND) {
                Log.d(LOG_TAG, "Distance to " + SoundList.soundList.get(i).getTitle() + ": " + distance);
                if (currentSound != sound) {
                    currentSound = sound;
                    prepareSound();
                }
            } else {
                if (currentSound == sound) {
                    currentSound = null;
                    prepareSound();
                }
            }
            if(distance > DISTANCE_TO_SOUND && distance <= DISTANCE_TO_NOTIFY) {
                if (backgroundSoundList.indexOf(sound) == -1) {
                    backgroundSoundList.add(sound);
                }
                Log.d(LOG_TAG, "Distance to " + SoundList.soundList.get(i).getTitle() + ": " + distance);
                notifyUser("Serendipity", "You're " + (int)distance + " m away from " + SoundList.soundList.get(i).getTitle() + ".");
            } else if (backgroundSoundList.indexOf(sound) != -1) {
                backgroundSoundList.remove(sound);
            }*/
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }
}

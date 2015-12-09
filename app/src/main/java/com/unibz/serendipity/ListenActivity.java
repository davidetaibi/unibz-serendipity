package com.unibz.serendipity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

import java.io.IOException;
import java.util.LinkedList;

public class ListenActivity extends AppCompatActivity implements LocationListener {
    private final String LOG_TAG = "LISTEN_ACTIVITY";
    private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_NOTIFY = 200;

    private GPSTracker gpsTracker;
    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private Sound currentSound;
    private LinkedList<Sound> backgroundSoundList = new LinkedList<Sound>();
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mediaPlayer = null;
        currentSound = null;
        prepared = false;
        playButton = (ImageButton) findViewById(R.id.play_button);

        initGPSTracking();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (result.length <= 0 || result[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Serendipity requires your location to work!!!", Toast.LENGTH_LONG).show();
                    initGPSTracking();
                }
                return;
            }
        }
    }

    private void initGPSTracking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            gpsTracker = new GPSTracker(this, this);
        }
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

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "LocationChanged", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "Location changed: lat: " + location.getLatitude() + "  long: " + location.getLongitude());

        double distance = 0.0;
        for (int i = 0; i < SoundList.soundList.size(); i++) {
            Sound sound = SoundList.soundList.get(i);
            distance  = sound.getDistance(location.getLatitude(), location.getLongitude());

            if (distance <= DISTANCE_TO_SOUND) {
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
            }
        }
    }

    private void notifyUser(String title, String contentText) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentTitle(title)
                .setContentText(contentText);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// Sets an ID for the notification, so it can be updated
        int notifyID = 1;

        mNotificationManager.notify(
                notifyID,
                mBuilder.build());

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(getApplicationContext(), "provider disabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Toast.makeText(getApplicationContext(), "provider enabled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //  Toast.makeText(getApplicationContext(), "status changed", Toast.LENGTH_LONG).show();
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

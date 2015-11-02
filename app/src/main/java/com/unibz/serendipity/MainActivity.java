package com.unibz.serendipity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final String LOG_TAG = "MAIN_ACTIVITY";

    private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_NOTIFY = 200;

    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private GPSTracker gpsTracker;
    private ArrayList<Sound> soundList;
    private Sound currentSound;
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = null;
        currentSound = null;
        prepared = false;
        playButton = (ImageButton) findViewById(R.id.play);
         
        initSounds();
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
    
    private void initSounds(){
        soundList = new ArrayList<Sound>();
        soundList.add(new Sound(getString(R.string.franziskaner), R.raw.franziskaner, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/franziskaner.mp3?raw=true", 46.500554, 11.353641));
        soundList.add(new Sound(getString(R.string.lido), R.raw.lido, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/lido.mp3?raw=true", 46.490593, 11.344708));
        soundList.add(new Sound(getString(R.string.museion), R.raw.museion, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/museion.mp3?raw=true", 46.497257, 11.348721));
        soundList.add(new Sound(getString(R.string.obstplatz),R.raw.obstplatz,"https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/obstplatz.mp3?raw=true", 46.499600, 11.352475));
        soundList.add(new Sound(getString(R.string.salewa), R.raw.salewa, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/salewa.mp3?raw=true", 46.470532, 11.314391));
        soundList.add(new Sound(getString(R.string.skatepark), R.raw.skatepark, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/skatepark.mp3?raw=true", 46.505322, 11.349811));

        //lat: 46.4977215  long: 11.3526115
        //soundList.add(new Sound("MyHome", R.raw.franziskaner, "https://github.com/davidetaibi/unibz-serendipity/blob/master/app/src/main/res/raw/franziskaner.mp3?raw=true", 46.4977215, 11.3526115));
    }

    private void initGPSTracking(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            gpsTracker = new GPSTracker(this, this);
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

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.location:
                if(gpsTracker.canGetLocation()) {
                    try {
                        Location currentLoc = gpsTracker.getLocation();
                        if (currentLoc != null) {
                            Toast.makeText(this, "Latitude: " + currentLoc.getLatitude() + " Longitude: " + currentLoc.getLongitude(), Toast.LENGTH_LONG).show();
                            onLocationChanged(currentLoc);
                        }
                    } catch (SecurityException e) {
                        Log.d(LOG_TAG, "SecurityException on GPSTracker.getLocation");
                    }
                }
                break;
            case R.id.play:
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
                    Log.d(LOG_TAG, "Sound " + currentSound.getName() + " prepared");
                    prepared = true;
                    playButton.setVisibility(View.VISIBLE);
                }
            });

            try {
                mediaPlayer.setDataSource(currentSound.getLink());
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
        for (int i = 0; i < soundList.size(); i++) {
            Sound sound = soundList.get(i);
            distance  = sound.getDistance(location.getLatitude(), location.getLongitude());

            if (distance <= DISTANCE_TO_SOUND) {
                Log.d(LOG_TAG, "Distance to " + soundList.get(i).getName() + ": " + distance);
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
                Log.d(LOG_TAG, "Distance to " + soundList.get(i).getName() + ": " + distance);
                notifyUser("Serendipity", "You're " + (int)distance + " m away from " + soundList.get(i).getName() + ".");
            }
        }
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
}

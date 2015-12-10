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

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final String LOG_TAG = "MAIN_ACTIVITY";

    private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_NOTIFY = 200;

    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private GPSTracker gpsTracker;
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

        initList();
        initGPSTracking();
         startActivity(new Intent(this, HomePageActivity.class));

          /** Intents to new Activities...
        startActivity(new Intent(this,MapsActivity.class));
        startActivity(new Intent(this,AboutActivity.class));
        startActivity(new Intent(this,RulesActivity.class));
        startActivity(new Intent(this,ContactsActivity.class));
         **/
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
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                if (result.length <= 0 || result[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Serendipity requires write permission to work!!!", Toast.LENGTH_LONG).show();
                    initList();
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

    private void initList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL);
        } else {
            SoundList soundList = new SoundList(this);
            soundList.download();
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
                Log.d(LOG_TAG, "Distance to " + SoundList.soundList.get(i).getTitle() + ": " + distance);
                notifyUser("Serendipity", "You're " + (int)distance + " m away from " + SoundList.soundList.get(i).getTitle() + ".");
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

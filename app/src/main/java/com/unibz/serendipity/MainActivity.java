package com.unibz.serendipity;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final String LOG_TAG = "MAIN_ACTIVITY";

    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_NOTIFY = 200;

    private MediaPlayer mediaPlayer;
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
        playButton = (ImageButton) findViewById(R.id.play);
        
        initSounds();
        initGPSTracking();
    }
    
    private void initSounds(){
        soundList = new ArrayList<Sound>();
        soundList.add(new Sound(getString(R.string.franziskaner),R.raw.franziskaner,46.500554, 11.353641));
        soundList.add(new Sound(getString(R.string.lido), R.raw.lido, 46.490593, 11.344708));
        soundList.add(new Sound(getString(R.string.museion), R.raw.museion, 46.497257, 11.348721));
        soundList.add(new Sound(getString(R.string.obstplatz),R.raw.obstplatz,46.499600, 11.352475));
        soundList.add(new Sound(getString(R.string.salewa), R.raw.salewa, 46.470532, 11.314391));
        soundList.add(new Sound(getString(R.string.skatepark), R.raw.skatepark, 46.505322, 11.349811));

        //lat: 46.76396308  long: 11.68467848
        //soundList.add(new Sound("MyHome", R.raw.franziskaner, 46.76396308, 11.68467848));
    }

    private void initGPSTracking(){
        gpsTracker = new GPSTracker(this,this);

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
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void prepareSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (currentSound != null) {
            mediaPlayer = MediaPlayer.create(this, currentSound.getSrcID());
            playButton.setVisibility(View.VISIBLE);
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

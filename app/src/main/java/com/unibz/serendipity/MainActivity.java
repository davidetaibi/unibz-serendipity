package com.unibz.serendipity;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.support.v7.app.NotificationCompat;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private MediaPlayer mediaPlayer;
    private GPSTracker gpsTracker;
    private ArrayList<Sound> soundList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = null;
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

    }

    private void initGPSTracking(){
        gpsTracker = new GPSTracker(this,this);

    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;

        super.onDestroy();
    }

    public void play(View view) {
        switch (view.getId()) {
            case R.id.franziskaner: Log.d("SerendipityPlayer", "Franziskaner");
                playSong(R.raw.franziskaner);
                break;
            case R.id.lido: Log.d("SerendipityPlayer", "Lido");
                playSong(R.raw.lido);
                break;
            case R.id.museion: Log.d("SerendipityPlayer", "Museion");
                playSong(R.raw.museion);
                break;
            case R.id.obstplatz: Log.d("SerendipityPlayer", "Obstplatz");
                playSong(R.raw.obstplatz);
                break;
            case R.id.salewa: Log.d("SerendipityPlayer", "Salewa");
                playSong(R.raw.salewa);
                break;
            case R.id.skatepark: Log.d("SerendipityPlayer", "Skatepark");
                playSong(R.raw.skatepark);
                break;
            case R.id.location:
                if(gpsTracker.canGetLocation()) {
                    Location currentLoc=  gpsTracker.getLocation();
                    Toast.makeText(getApplicationContext(), "Latitude: "+currentLoc.getLatitude()+" Longitude: "+currentLoc.getLongitude(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void playSong(int songID) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, songID);
        mediaPlayer.start();
    }

    @Override
    public void onLocationChanged(Location location) {
         Log.d("currLoc", "lat: " + location.getLatitude() + "  long: " + location.getLongitude());
        double distance=0.0;
        for (int i = 0;i<soundList.size();i++) {
             distance  = soundList.get(i).getDistance(location.getLatitude(),location.getLongitude());
            if(distance<200) { //200 only for testing

                notifyUser("Serendipity","You're "+(int)distance+" m away from "+soundList.get(i).getName()+".");
            }

            //Log.d("distanceFrom", ""+soundList.get(i).getName()+": "+distance);

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

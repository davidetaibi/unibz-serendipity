package com.unibz.serendipity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MAIN_ACTIVITY";

    private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGPSTracking();
        initList();
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
            new GPSTracker(this);
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
}

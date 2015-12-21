package com.unibz.serendipity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = "HOME_PAGE_ACTIVITY";

    private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_page);

        initGPSTracking();
        initList();

        Button signInButton= (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(this);

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(this);

        Button exploreButton = (Button) findViewById(R.id.exploreButton);
        exploreButton.setOnClickListener(this);

        Button listenButton = (Button) findViewById(R.id.listenButton);
        listenButton.setOnClickListener(this); //!!Only enable when user is in correct Location!!

        Button rulesButton = (Button) findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(this);

        Button citiesButton = (Button) findViewById(R.id.citiesButton);
        citiesButton.setOnClickListener(this);

        Button contactsButton = (Button) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(this);

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
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {*/
            new GPSTracker(this);
        //}
    }

    private void initList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL);
        } else {
            SoundList soundList = new SoundList(this);
            soundList.download();
        }
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.content_home_page, menu);
        return true;
    }

    private long mLastClickTime=0;
    @Override
    public void onClick(View v) {


        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v.getId() == R.id.signInButton) {
            Intent intent = new Intent(HomePageActivity.this, SignInActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.uploadButton) {
            Intent intent = new Intent(HomePageActivity.this, UploadActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.aboutButton) {
            Intent intent = new Intent(HomePageActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.exploreButton) {
            Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.listenButton) {
            Intent intent = new Intent(HomePageActivity.this, ListenActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.rulesButton) {
            Intent intent = new Intent(HomePageActivity.this, RulesActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.citiesButton) {
                Intent intent = new Intent(HomePageActivity.this, CitiesActivity.class);
                startActivity(intent);
        } else if (v.getId() == R.id.contactsButton) {
                Intent intent = new Intent(HomePageActivity.this, ContactsActivity.class);
                startActivity(intent);
        }

    }
}

/*if (v.getId() == R.id.searchButton) {
        Intent intent = new Intent(MapsActivity.this, CitiesActivity.class);
        startActivity(intent);
        }
        else if (v.getId() == R.id.playButton) {
        Intent intent = new Intent(MapsActivity.this, ListenActivity.class);
        startActivity(intent);
        }
        else if (v.getId() == R.id.lvButton) {
        Intent intent = new Intent(MapsActivity.this, HomePageActivity.class);
        startActivity(intent);
        }*/
package com.unibz.serendipity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

public class MapsActivity extends AppCompatActivity {
    private final String LOG_TAG = "MAPS_ACTIVITY";

    MapView mapView;
    GoogleMap map;
    private BroadcastReceiver locationChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        for(int i=0;i< SoundList.soundList.size();i++) {
            LatLng currLoc = new LatLng(SoundList.soundList.get(i).getLatitude(),SoundList.soundList.get(i).getLongitude());
            MarkerOptions currOpts = new MarkerOptions().position(currLoc).title(SoundList.soundList.get(i).getTitle());
            map.addMarker(currOpts);
            map.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
        }

        locationChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GPSTracker.ACTION)) {
                    Location newLocation = intent.getParcelableExtra(GPSTracker.EXTRA);
                    if (newLocation != null) {
                        locationChanged(newLocation);
                    }
                }
            }
        };

        loadLastKnown();

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CitiesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.lvButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadLastKnown() {
        Location currentLocation = GPSTracker.getCurrentLocation();
        if (currentLocation != null) {
            locationChanged(currentLocation);
        }
    }

    public void locationChanged(Location location) {
        //Toast.makeText(this, "LocationChanged", Toast.LENGTH_LONG).show();
        //Log.d(LOG_TAG, "Location changed: lat: " + location.getLatitude() + "  long: " + location.getLongitude());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onResume() {
        loadLastKnown();

        IntentFilter filter = new IntentFilter(GPSTracker.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, filter);

        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

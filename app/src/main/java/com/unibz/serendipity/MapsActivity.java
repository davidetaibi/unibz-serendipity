package com.unibz.serendipity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unibz.serendipity.utilities.SoundList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*Button searchButton= (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener((OnClickListener) this);
        Button playButton= (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener((OnClickListener) this);
        Button lvButton= (Button) findViewById(R.id.lvButton);
        lvButton.setOnClickListener((OnClickListener) this);}
        private long mLastClickTime=0;
        //@Override
        public void onClick(View v) {


            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if (v.getId() == R.id.searchButton) {
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

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for(int i=0;i< SoundList.soundList.size();i++) {
            LatLng currLoc = new LatLng(SoundList.soundList.get(i).getLatitude(),SoundList.soundList.get(i).getLongitude());
            MarkerOptions currOpts = new MarkerOptions().position(currLoc).title(SoundList.soundList.get(i).getTitle());
            mMap.addMarker(currOpts);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
        }

    }
}

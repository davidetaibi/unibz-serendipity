package com.unibz.serendipity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_page);

        Button signInButton= (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);

        Button uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(this);

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(this);

        Button exploreButton = (Button) findViewById(R.id.exploreButton);
        exploreButton.setOnClickListener(this);

        Button listenButton = (Button) findViewById(R.id.listenButton);
        listenButton.setOnClickListener(this);


        Button rulesButton = (Button) findViewById(R.id.rulesButton);
        rulesButton.setOnClickListener(this);

        Button citiesButton = (Button) findViewById(R.id.citiesButton);
        citiesButton.setOnClickListener(this);

        Button contactsButton = (Button) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(this);

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
            Log.d("clicks", "Sign In");
            Intent intent = new Intent(HomePageActivity.this, SignInActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.uploadButton) {
            Log.d("clicks", "Upload");
            //Intent intent = new Intent(HomePageActivity.this, UploadActivity.class);
            //startActivity(intent);
        } else if (v.getId() == R.id.aboutButton) {
            Log.d("clicks", "About");
            Intent intent = new Intent(HomePageActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.exploreButton) {
            Log.d("clicks", "Explore");
            Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.listenButton) {
            Log.d("clicks", "Cities");
            //Intent intent = new Intent(HomePageActivity.this, CitiesActivity.class);
            //startActivity(intent);
        } else if (v.getId() == R.id.rulesButton) {
            Log.d("clicks", "Rules");
            Intent intent = new Intent(HomePageActivity.this, RulesActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.citiesButton) {
                Log.d("clicks", "Maps");
                //Intent intent = new Intent(HomePageActivity.this, MapsActivity.class);
                //startActivity(intent);
        } else if (v.getId() == R.id.contactsButton) {
                Log.d("clicks", "Listen");
                Intent intent = new Intent(HomePageActivity.this, ContactsActivity.class);
                startActivity(intent);
        }

    }
}
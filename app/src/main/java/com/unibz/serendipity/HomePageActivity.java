package com.unibz.serendipity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.unibz.serendipity.AboutActivity;
import com.unibz.serendipity.ContactsActivity;
import com.unibz.serendipity.R;
import com.unibz.serendipity.RulesActivity;

import static android.support.v4.app.ActivityCompat.startActivity;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home_page);

        Button button= (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        Button button2= (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        Button button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.content_home_page, menu);
        return true;
    }

    private long mLastClickTime=0;
    @Override
    public void onClick(View v) {

        if (SystemClock.elapsedRealtime()- mLastClickTime < 1000){
            return;
        }
        mLastClickTime=SystemClock.elapsedRealtime();

        if(v.getId() == R.id.button) {
            Log.i("clicks", "Sign In");
            //Intent intent = new Intent(HomePageActivity.this, SignIn.class);
            //startActivity(intent);
        }
        else if(v.getId() == R.id.button2) {
            Log.i("clicks", "Rules");
            Intent intent = new Intent(HomePageActivity.this, RulesActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.button3) {
            Log.i("clicks", "About");
            Intent intent = new Intent(HomePageActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        else(v.getId() == R.id.button4) {
            Log.i("clicks", "Contacts");
            Intent intent = new Intent(HomePageActivity.this, ContactsActivity.class);
            startActivity(intent);
        }

    }

}
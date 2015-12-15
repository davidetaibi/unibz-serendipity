package com.unibz.serendipity;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.*;
import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addNew= (Button) findViewById(R.id.addNew);
        addNew.setOnClickListener((View.OnClickListener) this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private long mLastClickTime=0;
    //@Override
    public void onClick(View v) {


        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        if (v.getId() == R.id.addNew) {
            Intent intent = new Intent(CitiesActivity.this, UploadActivity.class);
            startActivity(intent);}

    }

    protected void onPostExecute(JSONArray result) {
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayList<String> cityList = (ArrayList<String>) getIntent().getSerializableExtra("cityList");

//private class FetchItems extends AsyncTask<String, Void, JSONArray>{
//        protected JSONArray doInBackground(String... params){
//            HttpClient httpClient = new DefaultHttpClient();
//
//            HttpGet httpGet = new HttpGet("");
//            httpGet.setHeader("Accept","application/json");
//            httpGet.setHeader("Content-type","application/json");
//
//            JSONArray json = new JSONArray();
//
//            try{
//                HttpResponse response = httpClient.execute(httpGet);
//
//                json = new JSONArray(EntityUtils.toString(response.getEntity()));
//                return json;
//            }catch (Exception e){
//                Log.v("Error adding ",e.getMessage());
//            }return json;
//        }
//
//        protected void onPostExecute(JSONArray result){
//            ListView lv = (ListView) findViewById(R.id.listView);
//            ArrayList<String> listItems = new ArrayList<String>();
//
//            for (int i=0;i<result.length();i++){
//                try {
//                    listItems.add(result.getJSONObject(i).getString("this").toString());
//                }catch (Exception e){
//                    Log.v("Error adding :", e.getMessage());
//                }
//            }
//            ArrayAdapter ad = new ArrayAdapter(CitiesActivity.this,android.R.layout.simple_gallery_item);
//            lv.setAdapter(ad);
//        }

    }}

package com.unibz.serendipity;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.*;
import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CitiesActivity extends AppCompatActivity {


    String session_name;
    String session_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras=getIntent().getExtras();
        session_name=extras.getString("session_name");
        session_id=extras.getString("session_id");

        new FetchItems().execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private class FetchItems extends AsyncTask<String, Void, JSONArray>{
        protected JSONArray doInBackground(String... params){
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("");
            httpGet.setHeader("Accept","application/json");
            httpGet.setHeader("Content-type","application/json");

            JSONArray json = new JSONArray();

            try{
                HttpResponse response = httpClient.execute(httpGet);

                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                return json;
            }catch (Exception e){
                Log.v("Error adding ",e.getMessage());
            }return json;
        }

        protected void onPostExecute(JSONArray result){
            ListView lv = (ListView) findViewById(R.id.listView);
            ArrayList<String> listItems = new ArrayList<String>();

            for (int i=0;i<result.length();i++){
                try {
                    listItems.add(result.getJSONObject(i).getString("this").toString());
                }catch (Exception e){
                    Log.v("Error adding :", e.getMessage());
                }
            }
            ArrayAdapter ad = new ArrayAdapter(CitiesActivity.this,android.R.layout.simple_gallery_item);
            lv.setAdapter(ad);
        }
    }


    public void addCity_click(View view){
            Intent intent = new Intent(this,UploadActivity.class);

            intent.putExtra("session_name",session_name);
            intent.putExtra("session_id",session_id);

            startActivity(intent);
    }

}

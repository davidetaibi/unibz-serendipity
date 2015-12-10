package com.unibz.serendipity;
//El Tisho was here!!!

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.os.AsyncTask;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    String session_name;
    String session_id;
    private class doSignIn extends AsyncTask<String, Integer, Long> {

        protected Integer doInBackground(String... params){
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("");

            try {
                EditText username = (EditText) findViewById(R.id.editName);
                EditText password = (EditText) findViewById(R.id.editPassword);

                JSONObject json = new JSONObject();
                json put("username",username.getText().toString().trim());
                json put("password",password.getText().toString().trim());

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);

                HttpResponse response = httpClient.execute(httpPost);

                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONObject jsonObj = new JSONObject(jsonResponse);

                session_name = jsonObj.getString("session_name");
                session_id = jsonObj.getString("session_id");

            }catch (Exception e){
                Log.v("Error logging in :", e.getMessage());
            }
            return 0;
        }
        protected void onPostExecute(Long result){
            Intent intent = new Intent(SignInActivity.this,CitiesActivity.class);

            intent.putExtra("session_name",session_name);
            intent.putExtra("session_id",session_id);

            startActivity(intent);
        }
    }

    public void doSignIn_click(View view){

        new doSignIn().execute();

    }
}

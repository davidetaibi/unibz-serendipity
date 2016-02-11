package com.unibz.serendipity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unibz.serendipity.utilities.LikeAsyncTask;
import com.unibz.serendipity.utilities.SoundList;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

 import com.facebook.FacebookCallback;

import com.facebook.login.LoginResult;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }


    public String session_name;
    public String session_id;
    public String token;



    //background task to login into Drupal
    private class LoginProcess extends AsyncTask<JSONObject, Integer, Integer> {

        protected Integer doInBackground(JSONObject... params) {

            HttpClient httpclient = new DefaultHttpClient();

            //set the remote endpoint URL
            HttpPost httppost = new HttpPost("http://sf.inf.unibz.it/serendipity/my_endpoint/user/login");


            try {

                JSONObject json = params[0];
                //add serialised JSON object into POST request
                StringEntity se = new StringEntity(json.toString());
                //set request content type
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                //send the POST request
                HttpResponse response = httpclient.execute(httppost);

                //read the response from Services endpoint
                String jsonResponse = EntityUtils.toString(response.getEntity());

                JSONObject jsonObject = new JSONObject(jsonResponse);
                //read the session information
                session_name=jsonObject.getString("session_name");
                session_id=jsonObject.getString("sessid");
                token=jsonObject.getString("token");

                Log.d("SIGNIN_TEST", "ID: " + session_id);
                Log.d("SIGNIN_TEST", "NAME: " + session_name);
                Log.d("SIGNIN_TEST", "TOKEN: " + token);

                return 0;

            }catch (Exception e) {
                Log.e("SIGNIN_TEST", e.getMessage());
                e.printStackTrace();
            }

            return 1;
        }


        protected void onPostExecute(Integer result) {
            if (result == 0) {
                CookieManager manager = (CookieManager) CookieHandler.getDefault();
                if (manager != null) {
                    Log.d("SIGNIN_COOKIE", "login successful");
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                    CookieStore mCookieStore = manager.getCookieStore();

                    try {
                        HttpCookie cookie = new HttpCookie(session_name, session_id);
                        cookie.setVersion(0);
                        cookie.setDomain("sf.inf.unibz.it");
                        cookie.setPath("/");
                        mCookieStore.add(new URI("http://sf.inf.unibz.it/"), cookie);

                        cookie = new HttpCookie("X-CSRF-Token", token);
                        cookie.setVersion(0);
                        cookie.setDomain("sf.inf.unibz.it");
                        cookie.setPath("/");
                        mCookieStore.add(new URI("http://sf.inf.unibz.it/"), cookie);

                        cookie = new HttpCookie("has_js", "1");
                        mCookieStore.add(new URI("http://sf.inf.unibz.it/"), cookie);

                        Log.d("SIGNIN_COOKIE", "cookies added:");
                        Log.d("SIGNIN_COOKIE", String.valueOf(manager.getCookieStore().getURIs().get(0)));

                        SoundList.download();

                        //create an intent to start the ListActivity
                        Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                        //pass the session_id and session_name to ListActivity
                        intent.putExtra("SESSION_ID", session_id);
                        intent.putExtra("SESSION_NAME", session_name);
                        //start the ListActivity
                        startActivity(intent);
                        finish();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("SIGNIN_COOKIE", "login FAILED: manager null");
                    Toast.makeText(getApplicationContext(), "Login FAILED", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("SIGNIN_COOKIE", "login FAILED");
                Toast.makeText(getApplicationContext(), "Login FAILED", Toast.LENGTH_LONG).show();
            }
        }
    }

    //click listener for doLogin button
//    public void doLoginButton_click(View view){
    public void doSignIn_click(View view) {
        //get the UI elements for username and password
        EditText username= (EditText) findViewById(R.id.editUsername);
        EditText password= (EditText) findViewById(R.id.editPassword);

        JSONObject json = new JSONObject();
        try {
            //extract the username and password from UI elements and create a JSON object
            json.put("username", username.getText().toString());
            json.put("password", password.getText().toString());
        } catch (Exception e) { e.printStackTrace(); }

        new LoginProcess().execute(json);
    }

    public void openReg(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }



}

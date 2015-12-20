package com.unibz.serendipity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.games.internal.GamesContract;

import com.loopj.android.http.*;
import org.json.JSONObject;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicCookieStore;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void saveUser_click(View view){
        EditText textName = (EditText) findViewById(R.id.textName);
        EditText textSurname = (EditText) findViewById(R.id.textSurname);
        EditText textNickname = (EditText) findViewById(R.id.textNickname);
        EditText textGender = (EditText) findViewById(R.id.textGender);
        EditText textEmail = (EditText) findViewById(R.id.textEmail);
        EditText textCity = (EditText) findViewById(R.id.textCity);
        EditText textPS = (EditText) findViewById(R.id.textPS);
        EditText textPassword = (EditText) findViewById(R.id.textPass);

        String name=textName.getText().toString().trim();
        String surname=textSurname.getText().toString().trim();
        String nickname=textNickname.getText().toString().trim();
        String gender=textGender.getText().toString().trim();
        String email=textEmail.getText().toString().trim();
        String city=textCity.getText().toString().trim();
        String PS=textPS.getText().toString().trim();
        String pass=textPassword.getText().toString().trim();
        new addUserTask().execute(name, nickname, email);
    }

    private class addUserTask extends AsyncTask<String, Void, Integer>{
        protected Integer doInBackground(String... params){
//            String session_name=params[0];
//            String session_id=params[1];
            String name = params[0];
            String surname = params[1];
            String nickname = params[2];
            String gender = params[3];
            String email = params[4];
            String city = params[5];
            String PS = params[6];
            String pass = params[7];

            for (String s : params) {
                Log.d("params: ", "params[]: " + s);
            }

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://sf.inf.unibz.it/serendipity/?q=my_endpoint/user/register");

            try {

                //EditText textCity = (EditText) findViewById(R.id.editCity);
                //EditText textPS = (EditText) findViewById(R.id.editPS);
                //EditText textPass = (EditText) findViewById(R.id.editPass);

                List<NameValuePair> nameValuePairs  =   new ArrayList<NameValuePair>();
                nameValuePairs.add( new BasicNameValuePair("account[username]", name));
                nameValuePairs.add( new BasicNameValuePair("account[surname]", surname));
                nameValuePairs.add( new BasicNameValuePair("account[nickname]", nickname));
                nameValuePairs.add( new BasicNameValuePair("account[gender]", gender));
                nameValuePairs.add( new BasicNameValuePair("account[email]", email));
                nameValuePairs.add( new BasicNameValuePair("account[city]", city));
                nameValuePairs.add( new BasicNameValuePair("account[PS]", PS));
                nameValuePairs.add( new BasicNameValuePair("account[password]", pass));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //Execute HTTP post request
                HttpResponse response   =   httpClient.execute(httpPost);

            }catch(Exception e){
                Log.e("HTTP ERROR", e.toString());
            }

//                StringEntity se = new StringEntity("{\"name\":\""+name+"\",\"type\":\"text\",\"email\":\""+email+"\",\"type\":\"email");
//                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                httpPost.setEntity(se);

//                BasicHttpContext mHttpContext = new BasicHttpContext();
                //CookieStore mCookieStore = new BasicCookieStore();

//                BasicClientCookie cookie = new BasicClientCookie(session_name, session_id);
//                cookie.setVersion(0);
//                cookie.setDomain("");
//                cookie.setPath("/");
//                mCookieStore.addCookie(cookie);
//                cookie = new BasicClientCookie("has_js", "1");
//                mCookieStore.addCookies(cookie);
//                mHttpContext.setAttribute(ClientContexts.COOKIE_STORE, mCookieStore);

//                httpClient.execute(httpPost,mHttpContext);

//                return 0;
//            }catch (Exception e){
//                Log.v("Error Registering User:", e.getMessage());
//            }
            return new Integer(0);
        }
    }
    }


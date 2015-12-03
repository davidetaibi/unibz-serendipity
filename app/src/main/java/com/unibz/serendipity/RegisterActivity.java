package com.unibz.serendipity;

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

import org.json.JSONObject;

import java.net.CookieStore;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
    public void saveUser_click(View view){
        new addUserTask().execute();
    }
    private class addUserTask extends AsyncTask<String, Void, Integer>{
        protected Integer doInBackground(String... params){
            String session_name=params[0];
            String session_id=params[1];

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("");

            try {
                EditText textName = (EditText) findViewById(R.id.editName);
                //EditText textSurname = (EditText) findViewById(R.id.editSurname);
                EditText textNickname = (EditText) findViewById(R.id.editNickname);
                //EditText textGender = (EditText) findViewById(R.id.editGender);
                EditText textEmail = (EditText) findViewById(R.id.editEmail);
                //EditText textCity = (EditText) findViewById(R.id.editCity);
                //EditText textPS = (EditText) findViewById(R.id.editPS);
                //EditText textPass = (EditText) findViewById(R.id.editPass);

                String name=textName.getText().toString().trim();
                String nickname=textNickname.getText().toString().trim();
                String email=textEmail.getText().toString().trim();

                StringEntity se = new StringEntity("{\"name\":\""+name+"","type":"text","email":""+email+"","type":"email");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);

                BasicHttpContext mHttpContext = new BasicHttpContext();
                CookieStore mCookieStore = new BasicCookieStore();

                BasicClientCookie cookie = new BasicClientCookie(session_name, session_id);
                cookie.setVersion(0);
                cookie.setDomain("");
                cookie.setPath("/");
                mCookieStore.addCookie(cookie);
                cookie = new BasicClientCookie("has_js", "1");
                mCookieStore.addCookies(cookie);
                mHttpContext.setAttribute(ClientContexts.COOKIE_STORE, mCookieStore);

                httpClient.execute(httpPost,mHttpContext);

                return 0;
            }catch (Exception e){
                Log.v("Error Registering User :", e.getMessage());
            }
        }
    }
    }


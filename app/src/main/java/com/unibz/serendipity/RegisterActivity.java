package com.unibz.serendipity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner spinner = (Spinner) findViewById(R.id.textGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item number: " + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    public void doReg_click(View view){
        EditText textName = (EditText) findViewById(R.id.textName);
        EditText textSurname = (EditText) findViewById(R.id.textSurname);
        EditText textUsername = (EditText) findViewById(R.id.textUsername);
        Spinner textGender = (Spinner) findViewById(R.id.textGender);
        EditText textEmail = (EditText) findViewById(R.id.textEmail);
        EditText textCity = (EditText) findViewById(R.id.textCity);
        EditText textPS = (EditText) findViewById(R.id.textPS);
        EditText textPassword = (EditText) findViewById(R.id.textPass);

        String firstname=textName.getText().toString().trim();
        String surname=textSurname.getText().toString().trim();
        String username=textUsername.getText().toString().trim();
        String gender=textGender.getPrompt().toString().trim();
        String email=textEmail.getText().toString().trim();
        String city=textCity.getText().toString().trim();
        String PS=textPS.getText().toString().trim();
        String pass=textPassword.getText().toString().trim();
        (new addUserTask(getApplicationContext())).execute(firstname, surname, username, gender, email, city, PS, pass);
    }

    private class addUserTask extends AsyncTask<String, Void, Integer>{
        private Context context;

        public addUserTask(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(String... params){

            String firstname = params[0];
            String surname = params[1];
            String username = params[2];
            String gender = params[3];
            String email = params[4];
            String city = params[5];
            String PS = params[6];
            String pass = params[7];

            for (String s : params) {
                Log.d("params: ", "params[]: " + s);
            }

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://sf.inf.unibz.it/serendipity/my_endpoint/user/register");

            try {

                List<NameValuePair> nameValuePairs  =   new ArrayList<NameValuePair>();
                //nameValuePairs.add( new BasicNameValuePair("firstname", name));
                //nameValuePairs.add(new BasicNameValuePair("surname", surname));
                nameValuePairs.add( new BasicNameValuePair("name", username));
                //nameValuePairs.add( new BasicNameValuePair("gender", gender));
                nameValuePairs.add( new BasicNameValuePair("mail", email));
                //nameValuePairs.add( new BasicNameValuePair("city", city));
                //nameValuePairs.add( new BasicNameValuePair("PS", PS));
                nameValuePairs.add( new BasicNameValuePair("pass[pass1]", pass));
                nameValuePairs.add( new BasicNameValuePair("pass[pass2]", pass));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //Execute HTTP post request
                HttpResponse response   =   httpClient.execute(httpPost);
                Log.d("HTTP ", "response: " + response);

                if (response.getStatusLine().getStatusCode() == 200) {
                    return 0;
                } else {
                    return -1;
                }

            }catch(Exception e){
                Log.e("HTTP ERROR", e.toString());
                return -1;
            }

//                StringEntity se = new StringEntity("{\"firstname\":\""+firstname+"\",\"type\":\"text\",\"email\":\""+email+"\",\"type\":\"email");
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
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer >= 0) {
                Toast.makeText(context, "Thanks for registering! Login and get going!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "ERROR! Please retry!", Toast.LENGTH_LONG).show();
            }
        }
    }
}


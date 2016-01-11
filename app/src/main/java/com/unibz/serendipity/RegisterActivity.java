package com.unibz.serendipity;

import android.os.AsyncTask;
import android.os.Bundle;
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
        Spinner spinner = (Spinner) findViewById(R.id.Gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item number: " + position, Toast.LENGTH_LONG).show();
            }
        });
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings2) {
            return true;}
        return super.onOptionsItemSelected(item);
    }*/




    public void doReg_click(View view){
        EditText textName = (EditText) findViewById(R.id.textName);
        EditText textSurname = (EditText) findViewById(R.id.textSurname);
        EditText textUsername = (EditText) findViewById(R.id.textUsername);
        Spinner textGender = (Spinner) findViewById(R.id.Gender);
        EditText textEmail = (EditText) findViewById(R.id.textEmail);
        EditText textCity = (EditText) findViewById(R.id.textCity);
        EditText textPS = (EditText) findViewById(R.id.textPS);
        EditText textPassword = (EditText) findViewById(R.id.textPass);

        String firstname=textName.getText().toString().trim();
        String surname=textSurname.getText().toString().trim();
        String username=textUsername.getText().toString().trim();
        String gender=textGender.getPrompt().toString();
        String email=textEmail.getText().toString().trim();
        String city=textCity.getText().toString().trim();
        String PS=textPS.getText().toString().trim();
        String pass=textPassword.getText().toString().trim();
        new addUserTask().execute(firstname, surname, username, gender, email, city, PS, pass);
    }

    private class addUserTask extends AsyncTask<String, Void, Integer>{
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

            }catch(Exception e){
                Log.e("HTTP ERROR", e.toString());
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
            return new Integer(0);
        }
    }
}


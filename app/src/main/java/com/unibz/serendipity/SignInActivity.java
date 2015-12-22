package com.unibz.serendipity;
//El Tisho was here!!!

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button regButton= (Button) findViewById(R.id.regButton);
        regButton.setOnClickListener(this);
    }

    String session_name;
    String session_id;

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.regButton){
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
    }else{
            return;
        }
    }

    private class doSignIn extends AsyncTask<String, Integer, Long> {


        private static final String BASE_URL = "https://sf.inf.unibz.it/serendipity/my_endpoint/user/login";
        private final AsyncHttpClient client = new AsyncHttpClient();

//        private static AsyncHttpClient client = new AsyncHttpClient();

        protected Long doInBackground(String... params){


//            client.get("https://www.google.com", new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    // called before request is started
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                    // called when response HTTP status is "200 OK"
//                }
//
////                @Override
////                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
////                    // If the response is JSONObject instead of expected JSONArray
////                }
////
////                @Override
////                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
////                    // Pull out the first event on the public timeline
////                    JSONObject firstEvent = timeline.get(0);
////                    String tweetText = firstEvent.getString("text");
////
////                    // Do something with the response
////                    System.out.println(tweetText);
////                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                }
//
//                @Override
//                public void onRetry(int retryNo) {
//                    // called when request is retried
//                }
//            });

            //Usage
            get("statuses/public_timeline.json", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    System.out.println("JSONObject response: " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline
                    String text = "";
                    try {
                        JSONObject firstEvent = (JSONObject) timeline.get(0);
                        text = firstEvent.getString("text");
                    } catch (Exception e) { e.printStackTrace(); }
                    // Do something with the response
                    System.out.println(text);
                }
            });






            return new Long(0);
//            HttpClient httpClient=new HttpClientBuilder.create().build();
//            HttpPost httpPost=new HttpPost("");
//
//            try {
//                EditText username = (EditText) findViewById(R.id.editName);
//                EditText password = (EditText) findViewById(R.id.editPassword);
//
//                JSONObject json = new JSONObject();
////                json.put("username", username.getText().toString().trim());
////                json put("password",password.getText().toString().trim());
//
//                json.put("username", params[0]);
//                json.put("password", params[1]);
//                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                httpPost.setEntity(se);
//
//                HttpResponse response = httpClient.execute(httpPost);
//
//                String jsonResponse = EntityUtils.toString(response.getEntity());
//
//                JSONObject jsonObj = new JSONObject(jsonResponse);
//
//                session_name = jsonObj.getString("session_name");
//                session_id = jsonObj.getString("session_id");
//
//            }catch (Exception e){
//                Log.v("Error logging in :", e.getMessage());
//            }
//            return new Long(0);
        }

        public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            url = "https://sf.inf.unibz.it/serendipity/?q=my_endpoint/user/login.json";
            params = new RequestParams();
            try{
                EditText username = (EditText) findViewById(R.id.editUsername);
                EditText password = (EditText) findViewById(R.id.editPassword);
               params.put("username", username.getText().toString().trim());
               params.put("password", password.getText().toString().trim());
            }catch (Exception e){
                Log.v("Error entering data : ", e.getMessage());
            }
            responseHandler = new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    //super.onSuccess(statusCode, headers, responseString);

                    //Log.e(TAG, "sucess: " + responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    //Log.e(TAG, "failure: " + responseString);
                    //Log.e(TAG, "failurecode: " + statusCode);

                    //super.onFailure(statusCode, headers, responseString, throwable);
                }
            };
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            //this.setHeaders();
            StringEntity se = null;
            try {
                se = new StringEntity(params.toString(), HTTP.UTF_8);
            } catch (Exception e) {
                e.getMessage();
            }
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            client.post(null, getAbsoluteUrl(url), se, "application/json", responseHandler);
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        private String getAbsoluteUrl(String relativeUrl) {
            return BASE_URL + relativeUrl;
        }





        protected void onPostExecute(Long result){
            Intent intent = new Intent(SignInActivity.this,CitiesActivity.class);

            intent.putExtra("session_name",session_name);
            intent.putExtra("session_id",session_id);

            startActivity(intent);
        }
    }

    public void doSignIn_click(View view) {

        EditText username = (EditText) findViewById(R.id.editUsername);
        EditText password = (EditText) findViewById(R.id.editPassword);



        new doSignIn().execute(username.getText().toString(), password.getText().toString());

    }


}

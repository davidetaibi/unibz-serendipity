package com.unibz.serendipity.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by FallenRiteMonk on 11/01/16.
 */
public class LikeAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    private final String LOG_TAG = "LIKE_UNLIKE_ASYNC_TASK";

    private Context context;

    public LikeAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        int entitityId = params[0];
        Log.d(LOG_TAG, "Like/Unlike node: id " + entitityId);

        HttpClient httpclient = new DefaultHttpClient();

        Log.d(LOG_TAG, "Cookies: " + ((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies().get(1).getValue());
        String token = ((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies().get(1).getValue();
        Log.d(LOG_TAG, "Token: " + token);


        HttpURLConnection urlConnection = null;
        try {
            JSONObject json = new JSONObject();
            JSONObject criteriaObject = new JSONObject();
            try {
                criteriaObject.put("entity_id", entitityId);
                criteriaObject.put("value_type", "points");
                criteriaObject.put("tag", "like");
                criteriaObject.put("value", "1");

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(criteriaObject);

                json.put("votes", jsonArray);
            } catch (Exception e) {
                e.printStackTrace(); }

            Log.d(LOG_TAG, "json: " + json.toString());

            URL url = new URL("http://sf.inf.unibz.it/serendipity/my_endpoint/votingapi/set_votes");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("x-csrf-token", token);
            urlConnection.connect();

            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            out.writeBytes(json.toString());
            out.flush();
            out.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d(LOG_TAG, "responseCode: " + responseCode);
            Log.d(LOG_TAG, "response: " + urlConnection.getResponseMessage());

            if (responseCode == 200) {
                Log.d(LOG_TAG, "Like successful");
                SoundList.download();
            } else {
                Log.d(LOG_TAG, "Like ERROR");
                Toast.makeText(context, "Error liking! try again!", Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

package com.unibz.serendipity.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fallenritemonk on 26/11/15.
 */
public class DownloadAsyncTask extends AsyncTask<String, Void, Boolean> {
    private final String LOG_TAG = "DOWNLOAD_ASYNC_TASK";

    private final Context context;
    private final Handler handler;

    private CookieManager mCookieManager;

    public DownloadAsyncTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;

        mCookieManager = ((CookieManager) CookieHandler.getDefault());
        if (mCookieManager == null) {
            mCookieManager = new CookieManager();
            CookieHandler.setDefault(mCookieManager);
            Log.d(LOG_TAG, "cookieManager was null: " + mCookieManager);
        } else {
            Log.d(LOG_TAG, "cookieManager: " + mCookieManager);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(LOG_TAG, "Download from: " + params[0]);
        Log.d(LOG_TAG, "Download to: " + params[1]);

        URL url = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            inputStream = new BufferedInputStream(url.openStream());
            outputStream = context.openFileOutput(params[1], Context.MODE_PRIVATE);

            int count;
            byte buffer[] = new byte[1024];

            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.flush();

            Log.d(LOG_TAG, "Download completed");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            handler.sendEmptyMessage(0);
        } else {
            handler.sendEmptyMessage(1);
        }
    }
}

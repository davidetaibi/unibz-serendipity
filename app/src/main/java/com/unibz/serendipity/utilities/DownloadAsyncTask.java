package com.unibz.serendipity.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fallenritemonk on 26/11/15.
 */
public class DownloadAsyncTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = "DOWNLOAD_ASYNC_TASK";

    private final Context context;
    private final Handler handler;

    public DownloadAsyncTask(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(LOG_TAG, "Download from: " + params[0]);
        Log.d(LOG_TAG, "Download to: " + params[1]);

        URL url = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            url = new URL(params[0]);
            URLConnection connection = url.openConnection();
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
        } catch (IOException e) {
            e.printStackTrace();
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
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        handler.sendEmptyMessage(0);
    }
}

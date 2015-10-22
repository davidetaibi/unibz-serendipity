package com.unibz.serendipity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer franziskanerPlayer;
    private MediaPlayer lidoPlayer;
    private MediaPlayer museionPlayer;
    private MediaPlayer obstplatzPlayer;
    private MediaPlayer salewaPlayer;
    private MediaPlayer skateparkPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        franziskanerPlayer = MediaPlayer.create(this, R.raw.franziskaner);
        lidoPlayer = MediaPlayer.create(this, R.raw.lido);
        museionPlayer = MediaPlayer.create(this, R.raw.museion);
        obstplatzPlayer = MediaPlayer.create(this, R.raw.obstplatz);
        salewaPlayer = MediaPlayer.create(this, R.raw.salewa);
        skateparkPlayer = MediaPlayer.create(this, R.raw.skatepark);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        franziskanerPlayer.release();
        lidoPlayer.release();
        museionPlayer.release();
        obstplatzPlayer.release();
        salewaPlayer.release();
        skateparkPlayer.release();
    }

    public void play(View view) {
        view.setBackgroundColor(Color.RED);
        switch (view.getId()) {
            case R.id.franziskaner: Log.d("SerendipityPlayer", "Franziskaner");
                startStop(franziskanerPlayer);
                break;
            case R.id.lido: Log.d("SerendipityPlayer", "Lido");
                startStop(lidoPlayer);
                break;
            case R.id.museion: Log.d("SerendipityPlayer", "Museion");
                startStop(museionPlayer);
                break;
            case R.id.obstplatz: Log.d("SerendipityPlayer", "Obstplatz");
                startStop(obstplatzPlayer);
                break;
            case R.id.salewa: Log.d("SerendipityPlayer", "Salewa");
                startStop(salewaPlayer);
                break;
            case R.id.skatepark: Log.d("SerendipityPlayer", "Skatepark");
                startStop(skateparkPlayer);
                break;
        }
    }

    private void startStop(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Log.d("SerendipityPlayer", "Stoped");
        } else {
            mediaPlayer.start();
            Log.d("SerendipityPlayer", "Started");
        }
    }
}

package com.unibz.serendipity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = null;
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;

        super.onDestroy();
    }

    public void play(View view) {
        switch (view.getId()) {
            case R.id.franziskaner: Log.d("SerendipityPlayer", "Franziskaner");
                playSong(R.raw.franziskaner);
                break;
            case R.id.lido: Log.d("SerendipityPlayer", "Lido");
                playSong(R.raw.lido);
                break;
            case R.id.museion: Log.d("SerendipityPlayer", "Museion");
                playSong(R.raw.museion);
                break;
            case R.id.obstplatz: Log.d("SerendipityPlayer", "Obstplatz");
                playSong(R.raw.obstplatz);
                break;
            case R.id.salewa: Log.d("SerendipityPlayer", "Salewa");
                playSong(R.raw.salewa);
                break;
            case R.id.skatepark: Log.d("SerendipityPlayer", "Skatepark");
                playSong(R.raw.skatepark);
                break;
        }
    }

    private void playSong(int songID) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, songID);
        mediaPlayer.start();
    }
}

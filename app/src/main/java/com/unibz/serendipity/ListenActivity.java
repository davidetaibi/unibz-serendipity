package com.unibz.serendipity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.unibz.serendipity.utilities.GPSTracker;
import com.unibz.serendipity.utilities.SoundList;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ListenActivity extends Activity {
    private final String LOG_TAG = "LISTEN_ACTIVITY";
    private final int DISTANCE_TO_SOUND = 10;
    private final int DISTANCE_TO_BACKGROUND = 200;

    private MediaPlayer mediaPlayer;
    private boolean prepared;
    private Sound currentSound;
    private LinkedList<Sound> reachableSoundList;
    private BroadcastReceiver locationChangeReceiver;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private TextView titleView;
    private TextView authorView;
    private ImageButton previousButton;
    private ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);

        currentSound = null;
        prepared = false;
        reachableSoundList = new LinkedList<>();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //Log.d(LOG_TAG, "Sound " + currentSound.getTitle() + " prepared");
                prepared = true;
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(LOG_TAG, "MEDIAPLAYER ERROR: what: " + what + " " + " extra: " + extra);
                Toast.makeText(getApplicationContext(), "MEDIAPLAYER ERROR: what: " + what + " " + " extra: " + extra, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        playButton = (ImageButton) findViewById(R.id.sound_play_button);
        pauseButton = (ImageButton) findViewById(R.id.sound_pause_button);
        previousButton = (ImageButton) findViewById(R.id.sound_previous_button);
        nextButton = (ImageButton) findViewById(R.id.sound_next_button);
        titleView = (TextView) findViewById(R.id.sound_title_view);
        authorView = (TextView) findViewById(R.id.sound_author_view);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(v);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(v);
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(v);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked(v);
            }
        });

        locationChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location newLocation = intent.getParcelableExtra(GPSTracker.EXTRA);
                if (newLocation != null) {
                    locationChanged(newLocation);
                }
            }
        };

        loadLastKnown();
    }

    private void loadLastKnown() {
        Location currentLocation = GPSTracker.getCurrentLocation();
        if (currentLocation != null) {
            locationChanged(currentLocation);
        }
    }

    public void locationChanged(Location location) {
        //Toast.makeText(this, "LocationChanged", Toast.LENGTH_LONG).show();
        //Log.d(LOG_TAG, "Location changed: lat: " + location.getLatitude() + "  long: " + location.getLongitude());

        reachableSoundList.clear();
        for (int i = 0; i < SoundList.soundList.size(); i++) {
            Sound sound = SoundList.soundList.get(i);
            if (sound.getDistance() <= DISTANCE_TO_BACKGROUND) {
                reachableSoundList.add(sound);
            }

            Collections.sort(reachableSoundList, new Comparator<Sound>() {
                @Override
                public int compare(Sound sound1, Sound sound2) {
                    return Double.compare(sound1.getDistance(), sound2.getDistance());
                }
            });
        }

        /*for (Sound sound : reachableSoundList) {
            Log.d(LOG_TAG, sound.getDistance() + "  " + sound.getTitle());
        }*/

        updatePlayer();
    }

    private void updatePlayer() {
        //Log.e(LOG_TAG, "updatePlayer");
        if (currentSound != null && reachableSoundList.indexOf(currentSound) == -1) {
            clearPlayer();
        }
        if (currentSound == null) {
            prepareSound(0);
        }
        if (currentSound != null) {
            updateNextPrevious();
        }
    }

    private void clearPlayer() {
        //Log.e(LOG_TAG, "clearPlayer");
        mediaPlayer.reset();
        currentSound = null;
        prepared = false;

        playButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        titleView.setText(getString(R.string.no_sound));
        authorView.setText(getString(R.string.around));
    }

    private void prepareSound(int direction) {
        //Log.e(LOG_TAG, "prepareSound");
        if (direction == 0 && reachableSoundList.size() != 0) {
            currentSound = reachableSoundList.getFirst();
            loadSound();
        } else if (direction != 0 && reachableSoundList.size() > 1) {
            int currentSoundIndex = reachableSoundList.indexOf(currentSound);
            clearPlayer();
            currentSound = reachableSoundList.get(currentSoundIndex + direction);
            loadSound();
        }
    }

    private void loadSound() {
        //Log.e(LOG_TAG, "LoadSound");
        String soundUrl;
        if (currentSound.getDistance() <= DISTANCE_TO_SOUND) {
            soundUrl = currentSound.getSoundLink();
            titleView.setText(currentSound.getTitle().toUpperCase());
        } else {
            soundUrl = currentSound.getBackgroundLink();
            titleView.setText(currentSound.getTitle().toUpperCase() + " [B]");
        }

        authorView.setText(currentSound.getCreaterName().toUpperCase());
        try {
            mediaPlayer.setDataSource(soundUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error on setting data source");
            e.printStackTrace();
        }
    }

    private void updateNextPrevious() {
        //Log.e(LOG_TAG, "updateNextPrevious");
        previousButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        int currentSoundIndex = reachableSoundList.indexOf(currentSound);
        if (currentSoundIndex == 0) {
            previousButton.setVisibility(View.INVISIBLE);
        }
        if (currentSoundIndex == reachableSoundList.size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.sound_play_button:
                playSound();
                break;
            case R.id.sound_pause_button:
                pauseSound();
                break;
            case R.id.sound_previous_button:
                prepareSound(-1);
                updateNextPrevious();
                break;
            case R.id.sound_next_button:
                prepareSound(1);
                updateNextPrevious();
                break;
        }
    }

    private void playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && prepared) {
            //Log.e(LOG_TAG, "Start playing sound");
            mediaPlayer.start();
            playButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    private void pauseSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying() && prepared) {
            //Log.e(LOG_TAG, "Stop playing sound");
            mediaPlayer.pause();
            playButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadLastKnown();

        IntentFilter filter = new IntentFilter(GPSTracker.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(locationChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationChangeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

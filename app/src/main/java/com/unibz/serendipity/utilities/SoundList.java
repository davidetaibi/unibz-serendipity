package com.unibz.serendipity.utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.unibz.serendipity.Sound;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fallenritemonk on 26/11/15.
 */
public class SoundList {
    private final static String LOG_TAG = "SOUND_LIST";
    private final static String LIST_URL = "http://sf.inf.unibz.it/serendipity/sounds";
    private final static String FILE_NAME = "all-sounds.xml";

    public static ArrayList<Sound> soundList = new ArrayList<Sound>();
    public static ArrayList<String> cityList = new ArrayList<String>();
    private static Context context = null;

    public SoundList(Context context) {
        this.context = context;
    }

    public void download() {
        Log.d(LOG_TAG, "Download initialized");
        new DownloadAsyncTask(context, new DownloadHandler()).execute(LIST_URL, FILE_NAME);
    }

    // developer.android.com/training/basics/network-ops/xml.html#read
    private static void createList() {
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(FILE_NAME);
            soundList = parse(inputStream);

            Toast.makeText(context.getApplicationContext(), "Soundlist Downloaded", Toast.LENGTH_LONG).show();

            Log.d(LOG_TAG, "SoundList:");
            for (int i = 0; i < soundList.size(); i++) {
                Log.d(LOG_TAG, soundList.get(i).toString());
            }

            Log.d(LOG_TAG, "CityList:");
            for (int i = 0; i < cityList.size(); i++) {
                Log.d(LOG_TAG, i + ": " + cityList.get(i).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Sound> parse(InputStream inputStream) throws XmlPullParserException, IOException{
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();
        return readSounds(parser);
    }

    private static ArrayList<Sound> readSounds(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Sound> sounds = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "sounds");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("sound")) {
                sounds.add(readSound(parser));
            } else {
                skip(parser);
            }
        }
        return sounds;
    }

    private static Sound readSound(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "sound");
        String id = "sound_id";
        String latitude = "Latitude";
        String longitude = "Longitude";
        String title = "title";
        String backgroundLink = "BackgroundSound";
        String soundLink = "MainSound";
        String createrName = "username";
        String city = "City";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals(id)) {
                id = readValue(parser, id);
            } else if (name.equals(latitude)) {
                latitude = readValue(parser, latitude);
            } else if (name.equals(longitude)) {
                longitude = readValue(parser, longitude);
            } else if (name.equals(title)) {
                title = readValue(parser, title);
            } else if (name.equals(backgroundLink)) {
                backgroundLink = readValue(parser, backgroundLink);
            } else if (name.equals(soundLink)) {
                soundLink = readValue(parser, soundLink);
            } else if (name.equals(createrName)) {
                createrName = readValue(parser, createrName);
            } else if (name.equals(city)) {
                city = readValue(parser, city);
                if (cityList.indexOf(city) == -1) {
                    cityList.add(city);
                }
            } else {
                skip(parser);
            }
        }
        return new Sound(Integer.valueOf(id), title, Double.valueOf(latitude), Double.valueOf(longitude), backgroundLink, soundLink, createrName);
    }

    private static String readValue(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tag);
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    static private class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                Log.d(LOG_TAG, "Download completed");
                createList();
            } else if (msg.what == 1) {
                Log.d(LOG_TAG, "Download error");
                createList();
            }
        }
    }
}

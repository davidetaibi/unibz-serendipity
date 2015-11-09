package com.unibz.serendipity.utilities;

import com.unibz.serendipity.Sound;

import java.util.ArrayList;

/**
 * Created by Felix on 02.11.15.
 */
public class SoundListCreator {
    public static ArrayList<Sound> soundList = new ArrayList<Sound>();



    public void addSound(String name, double lat, double lon, String link) {
        Sound sound = new Sound(name,lat,lon,link);
        soundList.add(sound);
    }

}

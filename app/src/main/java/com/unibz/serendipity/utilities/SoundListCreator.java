package com.unibz.serendipity.utilities;

import com.unibz.serendipity.Sound;

import java.util.ArrayList;

/**
 * Created by Cody on 02.11.15.
 */
public class SoundListCreator {
    public static ArrayList<Sound> soundList;


    public  SoundListCreator() {
        soundList = new ArrayList<Sound>();


    }
    public void addSound(String name, double lat, double lon, String link) {
        Sound sound = new Sound(name,lat,lon,link);
        soundList.add(sound);
    }
    public ArrayList<Sound> getSoundList() {
        return soundList;
    }

}

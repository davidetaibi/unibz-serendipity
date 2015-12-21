package com.unibz.serendipity;

import android.location.Location;

/**
 * Created by Cody on 26.10.15.
 */
public class Sound {
    private final int id;
    private double latitude;
    private double longitude;
    private String title;
    private String backgroundLink;
    private String soundLink;
    private String createrName;
    private boolean liked;
    private int likesCount;
    private double distanceToCurrent;

    public Sound(int newId, String newTitle, double newLat,double newLon, String newBackgroundLink, String newSoundLink, String newCreaterName, boolean newLiked, int newLikesCount) {
        this.id = newId;
        this.title = newTitle;
        this.latitude = newLat;
        this.longitude = newLon;
        this.backgroundLink = newBackgroundLink;
        this.soundLink = newSoundLink;
        this.createrName = newCreaterName;
        this.liked = newLiked;
        this.likesCount = newLikesCount;
        distanceToCurrent = -1;
    }

    public double getLatitude() {
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public String getTitle() {
        return this.title;
    }

    public String getCreaterName() {
        return createrName;
    }

    public String getBackgroundLink() {
        return backgroundLink;
    }
    public String getSoundLink() {
        return soundLink;
    }

    public String toString() {
        return id + ": "+this.title +" "+this.latitude+" "+this.longitude+" "+this.backgroundLink+" "+this.soundLink+" "+this.createrName+" liked:"+liked+" likes:"+likesCount;
    }

    public void setDistance(Location location) {
        distanceToCurrent = distFrom(this.latitude, this.longitude, location.getLatitude(), location.getLongitude());
    }

    public double getDistance() {
        return distanceToCurrent;
    }

    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

}

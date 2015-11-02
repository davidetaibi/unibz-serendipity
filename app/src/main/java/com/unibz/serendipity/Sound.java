package com.unibz.serendipity;

/**
 * Created by Cody on 26.10.15.
 */
public class Sound {
    private double latitude;
    private double longitude;
    private String name;
    private int srcID;
    private String link;

    public Sound(String newName,int newSrcID, String newLink, double newLat,double newLon) {
        this.name = newName;
        this.srcID = newSrcID;
        this.link = newLink;
        this.latitude = newLat;
        this.longitude = newLon;
    }

    public String getName() {
        return this.name;
    }

    public String getLink() {
        return link;
    }

    public int getSrcID() {
        return srcID;
    }

    public double getDistance(double currLat, double currLon) {

        return distFrom(this.latitude,this.longitude,currLat,currLon);
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

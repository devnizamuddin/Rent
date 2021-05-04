package com.example.rent.pojo;

import java.io.Serializable;

public class MyLocation implements Serializable {

    String lat;
    String lon;

    public MyLocation(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public MyLocation() {
    }

    public boolean isValid() {
        return lat != null && lon != null;
    }
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}

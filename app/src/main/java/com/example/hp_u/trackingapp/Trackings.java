package com.example.hp_u.trackingapp;

/**
 * Created by hp-u on 7/11/2017.
 */

public class Trackings {
    private int TrackingId;
    private String mobileno;
    private String latitude;
    private String longitude;
    private String tracking_time;
    private String tracking_date;
    private String address;
    public int getTrackingId() {
        return TrackingId;
    }
    public void setTrackingId(int trackingId) {
        TrackingId = trackingId;
    }
    public String getMobileno() {
        return mobileno;
    }
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getTracking_time() {
        return tracking_time;
    }
    public void setTracking_time(String tracking_time) {
        this.tracking_time = tracking_time;
    }
    public String getTracking_date() {
        return tracking_date;
    }
    public void setTracking_date(String tracking_date) {
        this.tracking_date = tracking_date;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}


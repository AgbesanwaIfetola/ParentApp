package com.example.ukeje.parentapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetData {

    @SerializedName("la")
    @Expose
    private double latitude;

    @SerializedName("lo")
    @Expose
    private double longitude;

    @SerializedName("date")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

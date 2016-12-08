package com.lyl.radian.DBObjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yannick on 29.10.2016.
 */

public class UserProfile {

    private String email;
    private String profilePic;
    private String location;
    private String language;
    private double latitude;
    private double longitude;
    //private HashMap<String, Object> participations;
    //private HashMap<String, Object> ownBids;

    public UserProfile() {

    }

    public UserProfile(String email, String profilePic, String location, String language, double latitude, double longitude) {
        this.email = email;
        this.profilePic = profilePic;
        this.location = location;
        this.language = language;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}


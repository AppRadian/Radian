package com.lyl.radian.Utilities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Yannick on 29.10.2016.
 */

public class UserProfile {

    private String email;
    private String location;
    private String language;
    private Bitmap profilePic;
    private ArrayList<String[]> participations;
    private ArrayList<String[]> ownBids;

    public UserProfile(){

    }

    public UserProfile(String email, String location, String language, Bitmap profilePic, ArrayList<String[]> participations, ArrayList<String[]> ownBids) {
        this.email = email;
        this.location = location;
        this.language = language;
        this.profilePic = profilePic;
        this.participations = participations;
        this.ownBids = ownBids;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<String[]> getParticipations() {
        return participations;
    }

    public void setParticipations(ArrayList<String[]> participations) {
        this.participations = participations;
    }

    public ArrayList<String[]> getOwnBids() {
        return ownBids;
    }

    public void setOwnBids(ArrayList<String[]> ownBids) {
        this.ownBids = ownBids;
    }
}


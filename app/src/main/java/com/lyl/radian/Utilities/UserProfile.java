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
    private ArrayList<UserProfile> participations;
    private ArrayList<Bid> ownBids;

    public UserProfile(){

    }

    public UserProfile(String email, String location, String language, Bitmap profilePic, ArrayList<UserProfile> participations, ArrayList<Bid> ownBids) {
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

    public ArrayList<UserProfile> getParticipations() {
        return participations;
    }

    public void setParticipations(ArrayList<UserProfile> participations) {
        this.participations = participations;
    }

    public ArrayList<Bid> getOwnBids() {
        return ownBids;
    }

    public void setOwnBids(ArrayList<Bid> ownBids) {
        this.ownBids = ownBids;
    }
}


package com.lyl.radian.DBObjects;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Yannick on 29.10.2016.
 */

public class UserProfile {

    private String email;
    private String location;
    private String language;
    private String profilePic;
    private ArrayList<Bid> participations;
    private ArrayList<Bid> ownBids;

    public UserProfile(){

    }

    public UserProfile(String email, String location, String language, String profilePic, ArrayList<Bid> participations, ArrayList<Bid> ownBids) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<Bid> getParticipations() {
        return participations;
    }

    public void setParticipations(ArrayList<Bid> participations) {
        this.participations = participations;
    }

    public ArrayList<Bid> getOwnBids() {
        return ownBids;
    }

    public void setOwnBids(ArrayList<Bid> ownBids) {
        this.ownBids = ownBids;
    }
}


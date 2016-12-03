package com.lyl.radian.DBObjects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yannick on 29.10.2016.
 */

public class UserProfile {

    private String email;
    private String location;
    private String language;
    //private HashMap<String, Object> participations;
    //private HashMap<String, Object> ownBids;

    public UserProfile() {

    }

    public UserProfile(String email, String location, String language/*, HashMap<String, Object> participations, HashMap<String, Object> ownBids*/) {
        this.email = email;
        this.location = location;
        this.language = language;
        //this.participations = participations;
        //this.ownBids = ownBids;
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

    /*
    public HashMap<String, Object> getParticipations() {
        return participations;
    }

    public void setParticipations(HashMap<String, Object> participations) {
        this.participations = participations;
    }

    public HashMap<String, Object> getOwnBids() {
        return ownBids;
    }

    public void setOwnBids(HashMap<String, Object> ownBids) {
        this.ownBids = ownBids;
    }*/
}


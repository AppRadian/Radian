package app.radiant.c.lly.Utilities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Yannick on 29.10.2016.
 */

public class UserProfile {

    private String email = "";
    private String location = "";
    private String language = "";
    private Bitmap profilePic;
    private double lat;
    private double lng;
    private ArrayList<String[]> participations = new ArrayList<>();
    private ArrayList<String[]> ownBids = new ArrayList<>();

    public String getEmail(){
        return email;
    }

    public String getLocation(){
        return location;
    }

    public String getLanguage(){
        return language;
    }

    public Bitmap getProfilePic(){ return profilePic; }

    public double getLat(){ return lat;}

    public double getLng() { return lng;}

    public ArrayList<String[]> getParticipations(){ return participations; }

    public ArrayList<String[]> getOwnBids(){ return ownBids; }

    public void setEmail(String email){
        this.email = email;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public void setProfilePic(Bitmap profilePic){ this.profilePic = profilePic; }

    public void setLat(double lat){ this.lat = lat;}

    public void setLng(double lng) { this.lng = lng;}

    public void setParticipations(ArrayList<String[]> l){
        participations = l;
    }

    public void setOwnBids(ArrayList<String[]> l){
        ownBids = l;
    }
}


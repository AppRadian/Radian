package com.lyl.radian.DBObjects;

/**
 * Created by Yannick on 02.12.2016.
 */

public class Bid {

    String id;
    String userId;
    String email;
    String tag;
    String description;
    String location;
    double latitude;
    double longitude;
    double averageRating;
    long count;
    String date;
    String time;
    long participants;
    long maxParticipants;
    public long distance;

    public Bid() {
    }

    public Bid(String id, String userId, String email, String tag, String description, String location, double latitude, double longitude, double averageRating, long count, String date, String time, long participants, long maxParticipants) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.tag = tag;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.averageRating = averageRating;
        this.count = count;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
    }

    // Compares 2 Bid Objects with their respective id which is unique
    // Must be implemented so List contains() method works as intended
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        // checks if bid is the same class (because if you compare a string with a bid dann kann mans auch gleich lassen)
        if (!Bid.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        // check if id's are equal
        final Bid other = (Bid) obj;
        if (id.equals(other.getId()))
            return true;

        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){ this.id = id; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getParticipants() {
        return participants;
    }

    public void setParticipants(long participants) {
        this.participants = participants;
    }

    public long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Bid setDistance(long distance){
        this.distance = distance;
        return this;
    }
}

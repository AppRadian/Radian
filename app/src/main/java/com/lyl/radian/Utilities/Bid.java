package com.lyl.radian.Utilities;

/**
 * Created by Yannick on 02.12.2016.
 */

public class Bid {

    String email;
    String tag;
    String description;
    String location;
    double averageRating;
    long count;
    String date;
    String time;
    long participants;
    long maxParticipants;

    public Bid() {
    }

    public Bid(String email, String tag, String description, String location, double averageRating, long count, String date, String time, long participants, long maxParticipants) {
        this.email = email;
        this.tag = tag;
        this.description = description;
        this.location = location;
        this.averageRating = averageRating;
        this.count = count;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
    }

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
}

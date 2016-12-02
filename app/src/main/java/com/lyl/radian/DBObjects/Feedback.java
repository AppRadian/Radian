package com.lyl.radian.DBObjects;

/**
 * Created by len13 on 02.12.2016.
 */

public class Feedback {
    String bidId;
    String fromUser;
    double rating;
    String text;

    public Feedback() {}

    public Feedback(String bidId, String fromUser, double rating, String text) {
        this.bidId = bidId;
        this.fromUser = fromUser;
        this.rating = rating;
        this.text = text;
    }

    public String getBidId() {
        return bidId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public double getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.lyl.radian.DBObjects;

/**
 * Created by len13 on 02.12.2016.
 */

public class Feedback {
    String bidId;
    String fromUserId;
    String fromUserMail;
    String toUserId;
    String toUserMail;
    double rating;
    String text;

    public Feedback() {}

    public Feedback(String bidId, String fromUserId, String fromUserMail, String toUserId, String toUserMail, double rating, String text) {
        this.bidId = bidId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.rating = rating;
        this.text = text;
        this.fromUserMail = fromUserMail;
        this.toUserMail = toUserMail;
    }

    public String getBidId() {
        return bidId;
    }

    public String getFromUserId() {
        return fromUserId;
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

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserMail() {
        return fromUserMail;
    }

    public void setFromUserMail(String fromUserMail) {
        this.fromUserMail = fromUserMail;
    }

    public String getToUserMail() {
        return toUserMail;
    }

    public void setToUserMail(String toUserMail) {
        this.toUserMail = toUserMail;
    }
}

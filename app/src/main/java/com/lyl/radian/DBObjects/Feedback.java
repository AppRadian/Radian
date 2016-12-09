package com.lyl.radian.DBObjects;

/**
 * Created by len13 on 02.12.2016.
 */

public class Feedback {
    String bidId;
    String fromUserId;
    String fromUserDisplayName;
    String toUserId;
    String toUserDisplayName;
    double rating;
    String text;

    public Feedback() {}

    public Feedback(String bidId, String fromUserId, String fromUserDisplayName, String toUserId, String toUserDisplayName, double rating, String text) {
        this.bidId = bidId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.rating = rating;
        this.text = text;
        this.fromUserDisplayName = fromUserDisplayName;
        this.toUserDisplayName = toUserDisplayName;
    }

    // Compares 2 Bid Objects with their respective id which is unique
    // Must be implemented so List contains() method works as intended
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        // checks if bid is the same class (because if you compare a string with a bid dann kann mans auch gleich lassen)
        if (!Feedback.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        // check if id's are equal
        final Feedback other = (Feedback) obj;
        if (bidId.equals(other.getBidId()))
            if (fromUserId.equals(other.getFromUserId()))
                return true;

        return false;
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

    public String getFromUserDisplayName() {
        return fromUserDisplayName;
    }

    public void setFromUserDisplayName(String fromUserDisplayName) {
        this.fromUserDisplayName = fromUserDisplayName;
    }

    public String getToUserDisplayName() {
        return toUserDisplayName;
    }

    public void setToUserDisplayName(String toUserDisplayName) {
        this.toUserDisplayName = toUserDisplayName;
    }
}

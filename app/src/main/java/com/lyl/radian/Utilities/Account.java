package com.lyl.radian.Utilities;

import android.app.Application;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;

import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.DBObjects.Feedback;
import com.lyl.radian.DBObjects.UserProfile;

/**
 * Created by Yannick on 26.10.2016.
 */

public class Account extends Application {

    public UserProfile self;
    public UserProfile searchedUser;
    public Bid clickedBid;
    public Feedback clickedFeedback;
    public LruCache<String, Bitmap> bitmapCache;
    public FragmentManager fm;

    /**
     * Initializes a cache for the bitmaps
     */
    public Account(){

        //Get max available VM Memory
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //use 1/8 of total memory for Bitmap cache
        final int cacheSize = maxMemory / 8;

        bitmapCache = new LruCache<String, Bitmap>(cacheSize){

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /**
     * Adds a bitmap to cache if not already in the cache
     * @param key - email of the user associated with this bitmap
     * @param bitmap - profile pic of the user
     */
    public void addBitmapToCache(String key, Bitmap bitmap){
        if(getBitmapFromCache(key) == null)
            bitmapCache.put(key, bitmap);
    }

    /**
     * Get bitmap with specific key from cache
     * @param key - email of the user associated with this bitmap
     * @return
     */
    public Bitmap getBitmapFromCache(String key){
        return bitmapCache.get(key);
    }

    /**
     * Look at return statement
     *
     * @return the Bid that is selected
     */
    public Bid getClickedBid() {
        return clickedBid;
    }

    public void setClickedBid(Bid clickedBid) {
        this.clickedBid = clickedBid;
    }

    public Feedback getClickedFeedback() {
        return clickedFeedback;
    }

    public void setClickedFeedback(Feedback clickedFeedback) {
        this.clickedFeedback = clickedFeedback;
    }

    /**
     * Look at return statement
     *
     * @return the Bid that is selected
     */
    public UserProfile getSearchedUser() {
        return searchedUser;
    }

    public void setSearchedUser(UserProfile searchedUser) {
        this.searchedUser = searchedUser;
    }

    public UserProfile getSelf() {
        return self;
    }

    public void setSelf(UserProfile self) {
        this.self = self;
    }
}
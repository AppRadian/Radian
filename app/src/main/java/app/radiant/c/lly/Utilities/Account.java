package app.radiant.c.lly.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.radiant.c.lly.Activities.FirebaseActivity;
import app.radiant.c.lly.Activities.ShowBidFeedbackActivity;
import app.radiant.c.lly.Fragments.BieteFragment;
import app.radiant.c.lly.Fragments.HomeFragment;
import app.radiant.c.lly.Fragments.OwnBidsFragment;
import app.radiant.c.lly.Fragments.ProfileFragment;
import app.radiant.c.lly.Fragments.SearchFragment;
import app.radiant.c.lly.Fragments.SearchItemFragment;
import app.radiant.c.lly.Fragments.SuperProfileFragment;
import app.radiant.c.lly.NetworkUtilities.AddBid;
import app.radiant.c.lly.NetworkUtilities.AddFeedback;
import app.radiant.c.lly.NetworkUtilities.AddInfo;
import app.radiant.c.lly.NetworkUtilities.DeleteBid;
import app.radiant.c.lly.NetworkUtilities.GetAccessToken;
import app.radiant.c.lly.NetworkUtilities.HomeShowBids;
import app.radiant.c.lly.NetworkUtilities.LoadBids;
import app.radiant.c.lly.NetworkUtilities.LoadOwnBids;
import app.radiant.c.lly.NetworkUtilities.Login;
import app.radiant.c.lly.NetworkUtilities.LoginWithAccessToken;
import app.radiant.c.lly.NetworkUtilities.Logout;
import app.radiant.c.lly.NetworkUtilities.Participate;
import app.radiant.c.lly.NetworkUtilities.SearchBid;
import app.radiant.c.lly.NetworkUtilities.SearchFeedback;
import app.radiant.c.lly.NetworkUtilities.SearchUser;
import app.radiant.c.lly.NetworkUtilities.UploadImage;
import app.radiant.c.lly.NetworkUtilities.EditBid;

/**
 * Created by Yannick on 26.10.2016.
 */

public class Account extends Application {

    private UserProfile self = null;
    private UserProfile searchedUser = new UserProfile();
    private SearchedItem searchedItem = null;
    private boolean searchSet = false;
    private String sessionId;
    public LruCache<String, Bitmap> bitmapCache;
    public FragmentManager fm;
    private String firebaseToken;

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

    public void setFirebaseToken(String token){

        firebaseToken = token;
    }

    public String getFirebaseToken(){

        return firebaseToken;
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
     * initializes a self UserProfile Object and calls AsyncTask to fetch user info
     * Login AsyncTask sets all required Attributes of UserProfile, also it calls getParticipations
     * and from there getAccesToken, which is needed to stay logged in
     * @param callingActivity
     * @param email
     * @param password
     */
    public void login(FirebaseActivity callingActivity, String email, String password) {

        self = new UserProfile();
        Login login = new Login(callingActivity, email, password);
        login.execute();
    }

    /**
     * Basically the same as login except for not logging in with a password but an Access Token
     * @param callingActivity
     * @param email
     * @param token
     */
    public void loginWithAccessToken(FirebaseActivity callingActivity, String email, String token){

        self = new UserProfile();
        LoginWithAccessToken login = new LoginWithAccessToken(callingActivity, email, token);
        login.execute();
    }

    /**
     * Gets Acces Token from Server so user stays logged in without safing his password on system
     * Gets called from Login or LoginWithAccesToken so it gets called after these tasks are finished
     * @param callingActivity
     */
    public void getAccessToken(Activity callingActivity){

        GetAccessToken token = new GetAccessToken(callingActivity, self.getEmail(), sessionId, self.getEmail());
        token.execute();
    }

    /**
     * Starts an AsyncTask Logout to let the Server know you are now offline.
     * Also this removes the Access Token from SharedPreferences so next time the app
     * gets started, user has to login with password again
     * @param callingActivity
     */
    public void logout(Activity callingActivity){

        Logout l = new Logout(callingActivity, self.getEmail(), sessionId);
        l.execute();
    }

    /**
     * Gets called from Logout AsyncTask to delete all info currently available in the memory.
     */
    public void deleteInfo(){

        sessionId = null;
        self = null;
        searchedUser = new UserProfile();
        searchedItem = null;
        searchSet = false;
        Constants.lastIdHome = "-1";
        Constants.lastIdOwnBids = "-1";
    }

    /**
     * Sets location and language attribute of UserProfile class
     * @param location
     * @param language
     */
    public void setSearchedUser(String location, String language){

        searchedUser.setLocation(location);
        searchedUser.setLanguage(language);
    }

    /**
     * Sets all values which identify a specific bid
     * @param context
     * @param id
     * @param email
     * @param tag
     * @param description
     * @param location
     * @param averageRating
     * @param count
     * @param distance
     * @param date
     * @param time
     * @param part
     * @param maxParticipators
     */
    public void setSearchedItem(Context context, String id, String email, String tag, String description, String location, String averageRating, String count, String distance, String date, String time, String part, String maxParticipators){

        searchSet = true;
        searchedItem = new SearchedItem(context, id, email, tag, description, location, averageRating, count, distance, date, time, part, maxParticipators);
    }

    //Getter
    public UserProfile getSelf(){ return self;}

    public UserProfile getSearchedUser() { return searchedUser; }

    public SearchedItem getSearchedItem() { return searchedItem; }

    /**
     * Puts the email and the sessionId in a HashMap.
     * This is needed for every request sent to the server
     * @return - HashMap with email and sessionId
     */
    public HashMap<String, String> getAuthMap(){
        HashMap<String,String> hm = new HashMap<>();
        hm.put("emailAuth", self.getEmail());
        hm.put("sessionId", sessionId);

        return hm;
    }

    //Setter
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }

    public String getSessionId(){
        return sessionId;
    }

    //TODO: Remove following methods
    public void addBid(Fragment callingFragment, String tag, String description, String location, double lat, double lng, String date, String time, String maxParticipators){

        AddBid a = new AddBid(callingFragment, self.getEmail(), sessionId, self.getEmail(), tag, description, location, lat, lng, date, time, maxParticipators);
        a.execute();
    }

    public void editBid(Fragment callingFragment, String id, String tag, String description, String location, double lat, double lng, String date, String time, String maxParticipators){

        EditBid e = new EditBid(callingFragment, self.getEmail(), sessionId, id, self.getEmail(), tag, description, location, lat, lng, date, time, maxParticipators);
        e.execute();
    }

    public void participate(SearchItemFragment callingFragment, String bidId, String email){

        Participate p = new Participate(callingFragment, self.getEmail(), sessionId, bidId, email);
        p.execute();
    }

    public void addFeedback(DialogFragment callingDialog, int id, String tag, String text, float rating){
        AddFeedback a = new AddFeedback(callingDialog, self.getEmail(), sessionId, id, tag, self.getEmail(), text, rating);
        a.execute();
    }

    public void uploadProfilePic(Activity callingActivity, Bitmap pic){

        UploadImage u = new UploadImage(callingActivity, self.getEmail(), sessionId, self.getEmail(), pic);
        u.execute();
    }

    public void editPassword(Activity callingActivity, String oldPw, String newPw){

        AddInfo a = new AddInfo(callingActivity, self.getEmail(), sessionId, self.getEmail(), "password", oldPw, newPw);
        a.execute();
    }

    public void editLocation(Activity callingActivity, String location){

        AddInfo a = new AddInfo(callingActivity, self.getEmail(), sessionId, self.getEmail(), "location", location);
        a.execute();
    }

    public void editLanguage(Activity callingActivity, String language){

        AddInfo a = new AddInfo(callingActivity, self.getEmail(), sessionId, self.getEmail(), "language", language);
        a.execute();
    }
}
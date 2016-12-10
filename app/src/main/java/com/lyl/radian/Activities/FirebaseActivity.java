package com.lyl.radian.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.UserProfile;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 01.12.2016.
 */

public class FirebaseActivity extends Activity {

    public Account account;
    // Befidet sich ein Nutzer in der MainAppActivity wurde er eingeloggt und wird hier
    // im Hintergrund in Firebase eingeloggt
    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener userAuthListener;
    private static final String TAG = "Firebase";
    private FirebaseDatabase database;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = (Account) getApplication();
        userAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        userAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:sign_in:" + user.getUid());
                    setContentView(R.layout.activity_splash_screen);

                    DatabaseReference id = users.child(userAuth.getCurrentUser().getUid()).child("registrationId");
                    id.setValue(FirebaseInstanceId.getInstance().getToken());

                    FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserProfile self = dataSnapshot.getValue(UserProfile.class);
                            account.setSelf(self);
                            Intent i = new Intent(FirebaseActivity.this, MainAppActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        userAuth.addAuthStateListener(userAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (userAuthListener != null) {
            userAuth.removeAuthStateListener(userAuthListener);
        }
    }

    public void createUser(final String email, final String displayname, final String location, final String language, final String password) {
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.e(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            final String email = userAuth.getCurrentUser().getEmail();

                            String profilePic = "images/" + email + System.currentTimeMillis();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayname)
                                    .setPhotoUri(Uri.parse(profilePic))
                                    .build();
                            userAuth.getCurrentUser().updateProfile(profileUpdates);

                            DatabaseReference thisUser = users.child(userAuth.getCurrentUser().getUid());
                            Location loc = Constants.getLocationFromAddress(FirebaseActivity.this, location);
                            UserProfile self;
                            if(loc != null)
                                self = new UserProfile(email, profilePic, location, language, loc.getLatitude(), loc.getLongitude());
                            else
                                self = new UserProfile(email, profilePic, location, language, 0, 0);
                            account.setSelf(self);
                            thisUser.setValue(account.getSelf());
                        }
                        else {
                            Log.e(TAG, "createUser:failed", task.getException());
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    public void signInUser(String email, String password) {
        userAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(userAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserProfile self = dataSnapshot.getValue(UserProfile.class);
                                    account.setSelf(self);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail:failed", task.getException());
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });
    }
}

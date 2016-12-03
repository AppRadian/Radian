package com.lyl.radian.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.DBObjects.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yannick on 01.12.2016.
 */

public class FirebaseActivity extends AppCompatActivity {

    public Account account;
    // Befidet sich ein Nutzer in der MainAppActivity wurde er eingeloggt und wird hier
    // im Hintergrund in Firebase eingeloggt
    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener userAuthListener;
    private static final String TAG = "Firebase";
    private FirebaseDatabase database;
    private DatabaseReference users;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = (Account) getApplication();
        userAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        userAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:sign_in:" + user.getUid());
                    Intent i = new Intent(FirebaseActivity.this, MainAppActivity.class);
                    startActivity(i);
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

    public void createUser(final String email, final String password) {
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.e(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            final String email = userAuth.getCurrentUser().getEmail();

                            DatabaseReference thisUser = users.child(userAuth.getCurrentUser().getUid());
                            thisUser.setValue(new UserProfile(email, null, null, "images/" + email + System.currentTimeMillis(), new HashMap<String, Object>(), new HashMap<String, Object>()));
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
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail:failed", task.getException());
                            Log.e(TAG, task.getException().getMessage());
                        }
                    }
                });
    }
}

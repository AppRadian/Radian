package com.lyl.radian.Fragments;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Activities.ShowBidFeedbackActivity;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.DBObjects.UserProfile;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 05.11.2016.
 */

public class SearchItemFragment extends Fragment{

    View view;
    Activity callingActivity;
    Account account;
    Button join;
    ImageView userProfile;
    TextView userEmail;
    TextView userBid;
    TextView timenDate;
    TextView userDescription;
    TextView ratings;
    RatingBar ratingBar;
    private final String TAG = "SearchItemFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_item, container, false);
        callingActivity = getActivity();

        AppBarLayout mToolbar = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        mToolbar.setTranslationY(0);

        account = (Account) callingActivity.getApplication();
        userProfile = (ImageView) view.findViewById(R.id.userProfile);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userBid = (TextView) view.findViewById(R.id.userBid);
        userDescription = (TextView) view.findViewById(R.id.userDescription);
        timenDate = (TextView) view.findViewById(R.id.timenDate);
        ratingBar = (RatingBar) view.findViewById(R.id.avergageRating);
        ratings = (TextView) view.findViewById(R.id.rezensionen);
        join = (Button) view.findViewById(R.id.joinButton);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(join.getText().toString().equals("Teilnehmen")) {
                    // Update the DB participants
                    DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                    bids.child(account.getClickedBid().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e(TAG, "got here add participant?");
                            // Get the bid object from the DB
                            final Bid bid = dataSnapshot.getValue(Bid.class);

                            // Extract the needed value
                            final long participants = bid.getParticipants();

                            // Get reference to this participations id to check if user already takes part or bid is full
                            DatabaseReference participations = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations").child(account.getClickedBid().getId());
                            participations.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String id = dataSnapshot.getValue(String.class);
                                    if(participants == bid.getMaxParticipants())
                                        return;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // set new participants
                            bid.setParticipants(participants + 1);

                            // Transfer update to DB
                            DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                            bids.child(account.getClickedBid().getId()).setValue(bid);


                            // Update user Object with participated events
                            DatabaseReference ownParts = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations");
                            ownParts.child(bid.getId()).setValue(bid.getId());
                            join.setText("Nicht mehr teilnehmen");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                // Delete Bid from participations and decrease participators in the particular bid
                else{
                    // Update the DB participants
                    DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                    bids.child(account.getClickedBid().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the bid object from the DB
                            Bid bid = dataSnapshot.getValue(Bid.class);

                            // Extract the needed value
                            long participants = bid.getParticipants();

                            // Get reference to this participations id to check if user already takes part
                            DatabaseReference participations = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations").child(bid.getId());

                            participants--;

                            // set new participants
                            bid.setParticipants(participants);

                            // Transfer update to DB
                            DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                            bids.child(account.getClickedBid().getId()).setValue(bid);

                            // Deletes the participation bid
                            DatabaseReference ownParts = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations");
                            ownParts.child(bid.getId()).removeValue();
                            join.setText("Teilnehmen");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + account.getClickedBid().getProfilePic());
        Glide.with(SearchItemFragment.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(userProfile);
        userEmail.setText(account.getClickedBid().getEmail());
        userBid.setText(account.getClickedBid().getTag());
        timenDate.setText(account.getClickedBid().getDate() + " - " + account.getClickedBid().getTime() + " Uhr");
        userDescription.setText(account.getClickedBid().getDescription());
        ratingBar.setRating((float)account.getClickedBid().getAverageRating());
        ratings.setText(account.getClickedBid().getCount() + " Rezensionen");

        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebot von " + account.getClickedBid().getEmail());

        // Sets button text to "Nicht mehr teilnehmen" if user already takes part
        DatabaseReference participations = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations").child(account.getClickedBid().getId());
        participations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getValue(String.class);
                if(id != null)
                    join.setText("Nicht mehr teilnehmen");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), ShowBidFeedbackActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowBidFeedbackActivity.class);
                startActivity(intent);
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(account.getClickedBid().getUserId());
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> hm = (HashMap<String, Object>)dataSnapshot.getValue();
                        String location = (String)hm.get("location");
                        String language = (String)hm.get("language");
                        HashMap<String, Object> bis = (HashMap<String, Object>)hm.get("ownBids");
                        //UserProfile u = new UserProfile(account.getClickedBid().getEmail(), location, language, account.getClickedBid().getProfilePic(), new HashMap<String, Object>(), new HashMap<String, Object>());
                        ProfileFragment f = new ProfileFragment();
                        account.fm.beginTransaction().replace(R.id.content_frame, f, "profile").addToBackStack("profile").commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return view;
    }

    public void setButtonText(String text){
        join.setText(text);
    }

   /* public boolean listContainsId(String id){

        for (String[] s : account.getSelf().getParticipations()){
            if(s[0].equals(id))
                return true;
        }
        return false;
    }*/
}

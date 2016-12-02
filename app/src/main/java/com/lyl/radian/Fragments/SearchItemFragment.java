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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_item, container, false);
        callingActivity = getActivity();

        AppBarLayout mToolbar = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        mToolbar.setTranslationY(0);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);

        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        content.setLayoutParams(p);

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
                    // Update the DB participants
                    DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                    bids.child(account.getClickedBid().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the bid object from the DB
                            Bid bid = dataSnapshot.getValue(Bid.class);

                            // Extract the needed value
                            long participants = bid.getParticipants();
                            participants++;

                            // set new participants
                            bid.setParticipants(participants);

                            // Transfer update to DB
                            DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                            bids.child(account.getClickedBid().getId()).setValue(bid);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                // Update user Object with participated events
                DatabaseReference users = FirebaseDatabase.getInstance().getReference(Constants.USER_DB);
                

            }
        });

       //TODO set profile pic
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + account.getClickedBid().getProfilePic());
        Glide.with(SearchItemFragment.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(userProfile);
        userEmail.setText(account.getClickedBid().getEmail());
        userBid.setText(account.getClickedBid().getTag());
        timenDate.setText(account.getClickedBid().getDate() + " - " + account.getClickedBid().getTime() + " Uhr");
        userDescription.setText(account.getClickedBid().getDescription());
        ratingBar.setRating((float)account.getClickedBid().getAverageRating());
        ratings.setText(account.getClickedBid().getCount() + " Rezensionen");

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        //((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebot von " + account.getSearchedItem().getEmail());
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);
/*
        if(listContainsId(account.getSearchedItem().getId()))
            join.setText("Nicht mehr teilnehmen");

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.participate(SearchItemFragment.this, account.getSearchedItem().getId(), account.getSearchedItem().getEmail());
            }
        });*/

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

        /*userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> data = account.getAuthMap();
                data.put("email", account.getSearchedItem().getEmail());
                new SearchUser(SearchItemFragment.this, data).execute();
            }
        });*/

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

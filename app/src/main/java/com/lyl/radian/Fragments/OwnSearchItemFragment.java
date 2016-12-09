package com.lyl.radian.Fragments;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.DialogFragments.EditDialog;
import com.lyl.radian.Activities.ShowBidFeedbackActivity;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by Ludwig on 20.11.2016.
 */

public class OwnSearchItemFragment extends Fragment {

        View view;
        Activity callingActivity;
        Account account;
        Button edit;
        ImageView userProfile;
        TextView userEmail;
        TextView userBid;
        TextView timenDate;
        TextView userDescription;
        TextView ratings;
        RatingBar ratingBar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_own_search_item, container, false);
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
            edit = (Button) view.findViewById(R.id.editButton);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
            Glide.with(OwnSearchItemFragment.this)
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .placeholder(R.drawable.blank_profile_pic)
                    .dontAnimate()
                    .into(userProfile);

            userEmail.setText(account.getClickedBid().getDisplayname());
            userBid.setText(account.getClickedBid().getTag());
            timenDate.setText(account.getClickedBid().getDate() + " - " + account.getClickedBid().getTime() + " Uhr");
            userDescription.setText(account.getClickedBid().getDescription());
            ratingBar.setRating((float)account.getClickedBid().getAverageRating());
            ratings.setText(account.getClickedBid().getCount() + " Rezensionen");

            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Dein Angebot");

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDialog add = new EditDialog();
                    add.show(getChildFragmentManager(), "Edit Dialog");
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

            return view;
        }

/*
        public boolean listContainsId(String id){

            for (String[] s : account.getSelf().getParticipations()){
                if(s[0].equals(id))
                    return true;
            }
            return false;
        }*/
    }


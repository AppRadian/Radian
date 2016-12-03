package com.lyl.radian.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterOwnProfile;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;
import com.lyl.radian.Widgets.NestedScrollViewFling;

/**
 * Created by Yannick on 03.11.2016.
 */

public class OwnBidsFragment extends SuperProfileFragment {

    ImageView profilePic;
    private FirebaseDatabase database;
    private DatabaseReference bids;
    ArrayList<Bid> bidsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        account = (Account) getActivity().getApplication();

        database = FirebaseDatabase.getInstance();
        bids = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ownBids");

        bids.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bid bid = dataSnapshot.getValue(Bid.class);
                        if(!bidsList.contains(bid))
                            bidsList.add(bid);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bid bid = dataSnapshot.getValue(Bid.class);
                        if(!bidsList.contains(bid))
                            bidsList.add(bid);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);
        adapter = new CustomRecyclerViewAdapterOwnProfile(this, bidsList);
        bidList = (RecyclerView) view.findViewById(R.id.cardList);
        bidList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bidList.setLayoutManager(llm);
        bidList.setAdapter(adapter);
        bidList.setNestedScrollingEnabled(false);
        ((NestedScrollViewFling)view.findViewById(R.id.nestedScrollView)).setOnTopReachedListener(new NestedScrollViewFling.OnFlingEndReachedTopListener()
        {
            @Override
            public void onTopReached(Boolean isBeingTouched)
            {
                if (!isBeingTouched)
                    expandToolbar();
            }
        });

        bidList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(position == 0)
                            return;

                        CustomRecyclerViewAdapterOwnProfile adapter = (CustomRecyclerViewAdapterOwnProfile) OwnBidsFragment.this.adapter;

                        account.setClickedBid(adapter.getItem(position));
                        OwnSearchItemFragment f = new OwnSearchItemFragment();
                        account.fm.beginTransaction().replace(R.id.content_frame, f, "OwnsearchItem").addToBackStack("OwnsearchItem").commit();                    }
                })
        );

        return view;
    }

    private void refresh() {

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
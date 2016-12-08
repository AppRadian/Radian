package com.lyl.radian.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.nfc.Tag;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Activities.SettingsActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterHome;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DialogFragments.BidDialog;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 03.11.2016.
 */

public class HomeFragment extends Fragment {

    View view;
    Activity callingActivity;
    Account account;
    Double[] latLong;
    public RecyclerView searches;
    public ArrayList<Bid> listItems = new ArrayList<>();
    public CustomRecyclerViewAdapterHome adapter;
    View.OnClickListener setLocation;
    public Comparator<Bid> cmp;
    private FirebaseDatabase database;
    private DatabaseReference bids;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        callingActivity = getActivity();
        account = (Account) callingActivity.getApplication();

        database = FirebaseDatabase.getInstance();
        bids = database.getReference("Bids");

        setLocation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setLocation = new Intent(getActivity(), SettingsActivity.class);
                startActivity(setLocation);
            }
        };

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);

        adapter = new CustomRecyclerViewAdapterHome(getActivity(), listItems);
        searches = (RecyclerView) view.findViewById(R.id.homeList);
        searches.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        searches.setLayoutManager(llm);
        searches.setAdapter(adapter);

        cmp = new Comparator<Bid>() {
            @Override
            public int compare(Bid o1, Bid o2) {
                long distance1 = o1.distance;
                long distance2 = o2.distance;
                if(distance1 < distance2)
                    return -1;
                else if(distance1 > distance2)
                    return 1;
                else
                    return 0;
            }
        };

        setLocation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setLocation = new Intent(getActivity(), SettingsActivity.class);
                startActivity(setLocation);
            }
        };

        searches.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                account.setClickedBid(adapter.getItem(position));
                SearchItemFragment f = new SearchItemFragment();
                account.fm.beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack("searchItem").commit();
            }
        }));

        return view;
    }

    private void refresh(){

        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebote in der Nähe");
        bids.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(account.getSelf().getLocation() == null) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Please set your location first", Snackbar.LENGTH_LONG)
                            .setAction("Set location", setLocation)
                            .setActionTextColor(Color.RED)
                            .show();
                    return;
                }
                final Bid bid = dataSnapshot.getValue(Bid.class);
                final Location bidLocation = new Location("bidLocation");
                bidLocation.setLatitude(bid.getLatitude());
                bidLocation.setLongitude(bid.getLongitude());

                final Location ownLocation = new Location("ownLocation");
                ownLocation.setLatitude(account.getSelf().getLatitude());
                ownLocation.setLongitude(account.getSelf().getLongitude());

                FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(bid.getUserId()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        bid.setProfilePic(dataSnapshot.getValue(String.class));
                        //Calculation down in the if clause so it has not to be calculated if any condition before is false
                        long distance;
                        if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && bid.getParticipants() < bid.getMaxParticipants() && (distance = Math.round(bidLocation.distanceTo(ownLocation))) <= 70000) {
                            if(listItems.contains(bid)) {
                                int index = listItems.indexOf(bid);
                                listItems.remove(bid);
                                listItems.add(index, bid.setDistance(distance));
                            }
                            else
                                listItems.add(bid.setDistance(distance));
                        }
                        else if(listItems.contains(bid))
                            listItems.remove(bid);

                        Collections.sort(listItems, cmp);
                        adapter.notifyDataSetChanged();
                        getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if(account.getSelf().getLocation() == null) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Please set your location first", Snackbar.LENGTH_LONG)
                            .setAction("Set location", setLocation)
                            .setActionTextColor(Color.RED)
                            .show();
                    return;
                }
                final Bid bid = dataSnapshot.getValue(Bid.class);
                Location bidLocation = new Location("");
                bidLocation.setLatitude(bid.getLatitude());
                bidLocation.setLongitude(bid.getLongitude());

                Location ownLocation = new Location("");
                ownLocation.setLatitude(account.getSelf().getLatitude());
                ownLocation.setLongitude(account.getSelf().getLongitude());

                //Calculation down in the if clause so it has not to be calculated if any condition before is false
                long distance;
                if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && bid.getParticipants() < bid.getMaxParticipants() && (distance = Math.round(bidLocation.distanceTo(ownLocation))) <= 70000) {
                    if(listItems.contains(bid)) {
                        int index = listItems.indexOf(bid);
                        listItems.remove(bid);
                        listItems.add(index, bid.setDistance(distance));
                    }
                    else
                        listItems.add(bid.setDistance(distance));
                }
                else if(listItems.contains(bid))
                    listItems.remove(bid);

                adapter.notifyDataSetChanged();
                getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, false);
    }
}

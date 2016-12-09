package com.lyl.radian.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Activities.SettingsActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterHome;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
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
    public RecyclerView searches;
    public ArrayList<Bid> listItems = new ArrayList<>();
    public CustomRecyclerViewAdapterHome adapter;
    View.OnClickListener setLocation;
    public Comparator<Bid> cmp;
    private FirebaseDatabase database;
    private DatabaseReference bids;
    ValueEventListener updateBids;
    public SwipeRefreshLayout swipeContainer;

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
                if(position >= adapter.getItemCount())
                    return;

                account.setClickedBid(adapter.getItem(position));
                SearchItemFragment f = new SearchItemFragment();
                account.fm.beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack("searchItem").commit();
            }
        }));

        updateBids = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(account.getSelf().getLocation() == null) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Please set your location first", Snackbar.LENGTH_LONG)
                                .setAction("Set location", setLocation)
                                .setActionTextColor(Color.RED)
                                .show();
                        return;
                    }
                    final Bid bid = data.getValue(Bid.class);
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
                            if(!bid.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && bid.getParticipants() < bid.getMaxParticipants() && (distance = Math.round(bidLocation.distanceTo(ownLocation))) <= 70000)
                                listItems.add(bid.setDistance(distance));

                            adapter.notifyDataSetChanged();
                            Collections.sort(listItems, cmp);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                view.findViewById(R.id.loading).setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateBids();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    private void refresh(){

        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebote in der Nähe");
        view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
        updateBids();
    }

    private void updateBids(){
        bids.removeEventListener(updateBids);
        bids.addListenerForSingleValueEvent(updateBids);
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

package com.lyl.radian.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterUpcoming;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;
import com.lyl.radian.Widgets.NestedScrollViewFling;

/**
 * Created by Yannick on 20.11.2016.
 */

public class UpcomingFragment extends SuperProfileFragment {

    ArrayList<Bid> upcomingEvents = new ArrayList<>();
    DatabaseReference user;
    DatabaseReference particpationsChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        account = (Account) getActivity().getApplication();

        // [BEGIN: Update this fragment with upcoming events from the DB]


        // Reference the right DB object -> in this case it's the user
        user = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Now reference from this user the "participations" child
        particpationsChild = user.child("participations");

        view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
        // The participatonsChild DBRef points to the child in the DB of this user that holds all participations (respectively all bidIds that the user joins)
        // Because we want retrieve every bidId that the user joins, we use the addChildEventListener and NOT the addListenerForSingleValueEvent
        particpationsChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Read the String with the bidId from the participations child
                String bidId = dataSnapshot.getValue(String.class);

                // Get bid-ref by referencing to the bid in the Bid-DB wit the associated bidId
                DatabaseReference aBid = FirebaseDatabase.getInstance().getReference("Bids").child(bidId);
                aBid.addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle the bid object associated wit the bid reference
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Bid bid = dataSnapshot.getValue(Bid.class);
                        Location bidLocation = new Location("bidLocation");
                        bidLocation.setLatitude(bid.getLatitude());
                        bidLocation.setLongitude(bid.getLongitude());

                        Location ownLocation = new Location("ownLocation");
                        ownLocation.setLatitude(account.getSelf().getLatitude());
                        ownLocation.setLongitude(account.getSelf().getLongitude());
                        final long distance = Math.round(bidLocation.distanceTo(ownLocation));

                        FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(bid.getUserId()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bid.setProfilePic(dataSnapshot.getValue(String.class));
                                if(!upcomingEvents.contains(bid))
                                    upcomingEvents.add(bid.setDistance(distance));
                                adapter.notifyDataSetChanged();
                                view.findViewById(R.id.loading).setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // Read the String with the bidId from the participations child
                String bidId = dataSnapshot.getValue(String.class);

                // Get bid-ref by referencing to the bid in the Bid-DB wit the associated bidId
                DatabaseReference aBid = FirebaseDatabase.getInstance().getReference("Bids").child(bidId);
                aBid.addListenerForSingleValueEvent(new ValueEventListener() {
                    // Handle the bid object associated wit the bid reference
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Bid bid = dataSnapshot.getValue(Bid.class);
                        Location bidLocation = new Location("bidLocation");
                        bidLocation.setLatitude(bid.getLatitude());
                        bidLocation.setLongitude(bid.getLongitude());

                        Location ownLocation = new Location("ownLocation");
                        ownLocation.setLatitude(account.getSelf().getLatitude());
                        ownLocation.setLongitude(account.getSelf().getLongitude());
                        final long distance = Math.round(bidLocation.distanceTo(ownLocation));

                        FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(bid.getUserId()).child("profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bid.setProfilePic(dataSnapshot.getValue(String.class));
                                if(!upcomingEvents.contains(bid))
                                    upcomingEvents.add(bid.setDistance(distance));
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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

        // [END: Update this fragment with upcoming events from the DB]

        // Previously added code by Yannick
        cmp = new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                String date1 = o1[8];
                String date2 = o2[8];
                String time1 = o1[9];
                String time2 = o2[9];

                String[] d1 = date1.split(Pattern.quote("/"));
                String[] d2 = date2.split(Pattern.quote("/"));
                String[] t1 = time1.split(Pattern.quote(":"));
                String[] t2 = time2.split(Pattern.quote(":"));

                int day1 = Integer.parseInt(d1[0]);
                int month1 = Integer.parseInt(d1[1]);
                int year1 = Integer.parseInt(d1[2]);
                int hours1 = Integer.parseInt(t1[0]);
                int minutes1 = Integer.parseInt(t1[1]);

                int day2 = Integer.parseInt(d2[0]);
                int month2 = Integer.parseInt(d2[1]);
                int year2 = Integer.parseInt(d2[2]);
                int hours2 = Integer.parseInt(t2[0]);
                int minutes2 = Integer.parseInt(t2[1]);

                if(year1 < year2)
                    return -1;
                else if(year1 > year2)
                    return 1;
                else {
                    if(month1 < month2)
                        return -1;
                    else if(month1 > month2)
                        return 1;
                    else {
                        if(day1 < day2)
                            return -1;
                        else if(day1 > day2)
                            return 1;
                        else {
                            if(hours1 < hours2)
                                return -1;
                            else if(hours1 > hours2)
                                return 1;
                            else {
                                if(minutes1 < minutes2)
                                    return -1;
                                else if(minutes1 > minutes2)
                                    return 1;
                                else
                                    return 0;
                            }
                        }
                    }
                }
            }
        };

        //Collections.sort(account.getSelf().getParticipations(), cmp);

        adapter = new CustomRecyclerViewAdapterUpcoming(getActivity(), upcomingEvents);
        bidList = (RecyclerView) view.findViewById(R.id.cardList);
        bidList.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bidList.setLayoutManager(llm);
        bidList.setAdapter(adapter);
        bidList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, true);
                    }
                }
            }
        });

        bidList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(position == 0)
                            return;

                        CustomRecyclerViewAdapterUpcoming adapter = (CustomRecyclerViewAdapterUpcoming) UpcomingFragment.this.adapter;

                        account.setClickedBid(adapter.getItem(position));
                        SearchItemFragment f = new SearchItemFragment();
                        account.fm.beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack("searchItem").commit();
                    }
                })
        );

        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
    }
}

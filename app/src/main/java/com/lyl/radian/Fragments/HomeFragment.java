package com.lyl.radian.Fragments;

import android.app.Activity;
import android.nfc.Tag;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    public Comparator<String[]> cmp;
    public SwipeRefreshLayout swipeContainer;
    FloatingActionButton fab;
    private FirebaseDatabase database;
    private DatabaseReference bids;
    public final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        callingActivity = getActivity();
        account = (Account) callingActivity.getApplication();

        database = FirebaseDatabase.getInstance();
        bids = database.getReference("Bids");

        bids.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                final Bid bid = dataSnapshot.getValue(Bid.class);
                if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !listItems.contains(bid) &&
                        bid.getParticipants() < bid.getMaxParticipants())
                    listItems.add(bid);

                adapter.notifyDataSetChanged();

                /*
                // Check if the participants are max, if so than remove the bid from the listItem
                // if the current user is NOT a participant
                DatabaseReference user = FirebaseDatabase.getInstance().getReference(Constants.USER_DB);
                DatabaseReference myParticipations = user.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("participations");

                // Add a listener that scans your child
                myParticipations.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> bidId = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (bidId != null) {
                            Object o = bidId.get(bid.getId());
                            if (o == null && bid.getCount() > bid.getMaxParticipants()) {
                                //Log.e(TAG, o.toString());
                                listItems.remove(bid);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, String.valueOf(bid.getCount()));
                            if(bid.getCount() > bid.getMaxParticipants()) {
                                Log.e(TAG, String.valueOf(bid.getCount()));
                                listItems.remove(bid);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


                // Notify adapter
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Bid bid = dataSnapshot.getValue(Bid.class);
                //TODO Distanz berrechnen
                //latLong = Constants.getLocationFromAddress(getActivity(), bid.getLocation());
                if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !listItems.contains(bid))
                    listItems.add(bid);
                adapter.notifyDataSetChanged();
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

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);

        adapter = new CustomRecyclerViewAdapterHome(getActivity(), listItems);
        searches = (RecyclerView) view.findViewById(R.id.homeList);
        searches.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        searches.setLayoutManager(llm);
        searches.setAdapter(adapter);

        cmp = new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                int distance1 = Integer.parseInt(o1[7]);
                int distance2 = Integer.parseInt(o2[7]);
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

    public String getStringIdFromObject(String s) {
        LinkedList<String> list = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '=') {
                list.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }

}

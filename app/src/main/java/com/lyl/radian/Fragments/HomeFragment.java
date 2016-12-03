package com.lyl.radian.Fragments;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Activities.SettingsActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterHome;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DialogFragments.BidDialog;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;

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

                Bid bid = dataSnapshot.getValue(Bid.class);
                if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !listItems.contains(bid))
                    listItems.add(bid);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Bid bid = dataSnapshot.getValue(Bid.class);
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

}

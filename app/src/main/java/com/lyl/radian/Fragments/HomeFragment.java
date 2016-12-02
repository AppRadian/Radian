package com.lyl.radian.Fragments;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.lyl.radian.Widgets.HidingScrollListener;

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

        listItems.clear();
        bids.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                
                Bid bid = dataSnapshot.getValue(Bid.class);
                if(!bid.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    listItems.add(bid);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidDialog add = new BidDialog();
                add.show(getChildFragmentManager(), "Biete Dialog");
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
        searches.setOnScrollListener(new HidingScrollListener() {
            AppBarLayout mToolbar = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
            @Override
            public void onHide() {
                fab.hide();
                mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            @Override
            public void onShow() {
                fab.show();
                mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            }
        });

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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);


        return view;
    }

    private void refresh(){

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(null);
        content.setLayoutParams(p);

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebote in deiner Nähe");
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_home);
    }
}

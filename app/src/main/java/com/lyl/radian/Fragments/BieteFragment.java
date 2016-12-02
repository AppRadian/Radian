package com.lyl.radian.Fragments;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterBiete;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DialogFragments.BidDialog;
import com.lyl.radian.DialogFragments.MyDialogCloseListener;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.Widgets.HidingScrollListener;

/**
 * Created by Yannick on 03.11.2016.
 */

public class BieteFragment extends Fragment implements MyDialogCloseListener {

    View view;
    MainAppActivity callingActivity;
    RecyclerView bieteList;
    ArrayList<Bid> bidsList = new ArrayList<>();
    public CustomRecyclerViewAdapterBiete adapter;
    Account account;
    FloatingActionButton fab;
    private FirebaseDatabase database;
    private DatabaseReference bids;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biete, container, false);
        callingActivity = (MainAppActivity) getActivity();

        account = (Account) callingActivity.getApplication();

        database = FirebaseDatabase.getInstance();
        bids = database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ownBids");


        bidsList.clear();
        bids.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.e("childAdded","childAdded");
                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bid bid = dataSnapshot.getValue(Bid.class);
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

                Log.e("childChanged","childChanged");
                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bid bid = dataSnapshot.getValue(Bid.class);
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

        adapter = new CustomRecyclerViewAdapterBiete(this, bidsList);
        bieteList = (RecyclerView) view.findViewById(R.id.bieteList);
        bieteList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bieteList.setLayoutManager(llm);
        bieteList.setAdapter(adapter);
        bieteList.setOnScrollListener(new HidingScrollListener() {
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


        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);
        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidDialog add = new BidDialog();
                add.show(getChildFragmentManager(), "Biete Dialog");
            }
        });

        bieteList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

                account.setClickedBid(adapter.getItem(position));
                OwnSearchItemFragment f = new OwnSearchItemFragment();
                account.fm.beginTransaction().replace(R.id.content_frame, f, "OwnsearchItem").addToBackStack("OwnsearchItem").commit();
            }
        }));

        return view;
    }

    private void refresh(){

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(null);
        content.setLayoutParams(p);

        bidsList.clear();
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Deine Angebote");
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_biete);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        refresh();
    }
}

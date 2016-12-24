package com.lyl.radian.Fragments;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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

        adapter = new CustomRecyclerViewAdapterBiete(this, bidsList);
        bieteList = (RecyclerView) view.findViewById(R.id.bieteList);
        bieteList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bieteList.setLayoutManager(llm);
        bieteList.setAdapter(adapter);

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

        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Deine Angebote");
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        //((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_biete);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        refresh();
    }
}

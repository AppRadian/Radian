package com.lyl.radian.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Comparator;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.lyl.radian.Activities.ChatActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapter;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;
import com.lyl.radian.Widgets.NestedScrollViewFling;
import com.lyl.radian.Widgets.ScrollingFABBehavior;

/**
 * Created by len13 on 17.10.2016.
 */

public class ProfileFragment extends SuperProfileFragment {

    ImageView profilePic;
    FloatingActionButton sendMessage;
    private FirebaseDatabase database;
    private DatabaseReference bids;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        account =(Account) getActivity().getApplication();

        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);

        adapter = new CustomRecyclerViewAdapter(getActivity(), bieteItems);
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
        database = FirebaseDatabase.getInstance();
        bids = database.getReference("Users").child(account.getClickedBid().getUserId()).child("ownBids");

        bids.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                if(!bieteItems.contains(bid))
                                    bieteItems.add(bid.setDistance(distance));
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
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String bidId = dataSnapshot.getValue(String.class);
                DatabaseReference ownBids = database.getReference("Bids").child(bidId);
                ownBids.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                if(!bieteItems.contains(bid))
                                    bieteItems.add(bid.setDistance(distance));
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

        bidList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(position == 0)
                            return;

                        CustomRecyclerViewAdapter adapter = (CustomRecyclerViewAdapter)ProfileFragment.this.adapter;


                        account.setClickedBid(adapter.getItem(position));
                        SearchItemFragment f = new SearchItemFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack(null).commit();
                    }
                })
        );

        sendMessage = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        sendMessage.setImageResource(R.drawable.ic_menu_send);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });


        setCollapsingToolbarEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + account.getClickedBid().profilePic);
        getActivity().findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Glide.with(ProfileFragment.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                        getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
                        return false;
                    }
                })
                .fitCenter()
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(profilePic);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        setCollapsingToolbarEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCollapsingToolbarEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + account.getClickedBid().profilePic);
        getActivity().findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Glide.with(ProfileFragment.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                        getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        getActivity().findViewById(R.id.loading).setVisibility(View.GONE);
                        return false;
                    }
                })
                .fitCenter()
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(profilePic);
    }

    private void setCollapsingToolbarEnabled(boolean enabled){
        final Resources r = getResources();
        final int px = (int)(r.getDisplayMetrics().heightPixels / 2.5);
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar);

        if(enabled){
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

            ((FloatingActionButton)getActivity().findViewById(R.id.fab2)).hide();
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) getActivity().findViewById(R.id.fab2).getLayoutParams();
            p.setBehavior(null);
            getActivity().findViewById(R.id.fab2).setLayoutParams(p);
            sendMessage.setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("");
            toolbar.setTitleEnabled(true);
            profilePic.setMaxHeight(px);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, false);
            toolbar.setTitle(account.getClickedBid().getEmail());
        }
        else{
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

            ((FloatingActionButton)getActivity().findViewById(R.id.fab2)).show();
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) getActivity().findViewById(R.id.fab2).getLayoutParams();
            p.setBehavior(new ScrollingFABBehavior());
            getActivity().findViewById(R.id.fab2).setLayoutParams(p);
            sendMessage.setVisibility(View.GONE);
            toolbar.setTitleEnabled(false);
            profilePic.setMaxHeight(0);
            profilePic.setImageBitmap(null);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, false);
        }
    }
}

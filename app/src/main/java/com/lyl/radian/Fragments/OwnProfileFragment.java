package com.lyl.radian.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Activities.SettingsActivity;
import com.lyl.radian.Adapter.PagerAdapter;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 03.11.2016.
 */

public class OwnProfileFragment extends Fragment {

    View view;
    Account account;
    FloatingActionButton settings;
    ImageView profilePic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ownprofile_tab, container, false);

        account = (Account) getActivity().getApplication();

        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);
        settings = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        settings.setImageResource(R.drawable.ic_menu_manage);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivity = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsActivity);
            }
        });

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.VISIBLE);
        if(tabLayout.getTabCount() != 2) {
            tabLayout.addTab(tabLayout.newTab().setText("Angebote"));
            tabLayout.addTab(tabLayout.newTab().setText("Anstehende"));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void refresh() {

        final Resources r = getResources();
        final int px = r.getDisplayMetrics().heightPixels / 3;

        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("profilePic")) {
                    String profilePic = (String)dataSnapshot.getValue();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + profilePic);
                    Glide.with(OwnProfileFragment.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .override(r.getDisplayMetrics().widthPixels, px)
                            .placeholder(R.drawable.blank_profile_pic)
                            .into(OwnProfileFragment.this.profilePic);
                            //.into(target);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("profilePic")) {
                    /**
                    String profilePic = (String)dataSnapshot.getValue();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + profilePic);
                    Glide.with(OwnProfileFragment.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .override(r.getDisplayMetrics().widthPixels, px)
                            .centerCrop()
                            .into(OwnProfileFragment.this.profilePic);
                     **/
                }
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
        setCollapsingToolbarEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_own_profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        setCollapsingToolbarEnabled(false);
    }

    private boolean isProfilePrevious(){
        int index = account.fm.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry;
        String tag = "";
        if(index > 0) {
            backEntry = account.fm.getBackStackEntryAt(index);
            if(backEntry != null)
                tag = backEntry.getName();
        }
        if(tag != null && tag.equals("profile"))
            return true;

        return false;
    }

    private void setCollapsingToolbarEnabled(boolean enabled){
        final Resources r = getResources();
        final int px = r.getDisplayMetrics().heightPixels / 3;

        if(enabled){
            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("");
            ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(true);
            profilePic.setMaxHeight(px);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true);
            ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitle("Dein Profil");
        }
        else{
            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
            tabLayout.setVisibility(TabLayout.GONE);

            ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
            profilePic.setMaxHeight(0);
            profilePic.setImageBitmap(null);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(false);
        }
    }
}

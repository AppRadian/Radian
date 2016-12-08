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
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
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
import com.lyl.radian.Widgets.NestedScrollViewFling;

/**
 * Created by Yannick on 03.11.2016.
 */

public class OwnProfileFragment extends Fragment {

    View view;
    Account account;
    ImageView profilePic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ownprofile_tab, container, false);

        account = (Account) getActivity().getApplication();

        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("hallo","unselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("hallo","reslected");
            }
        });
        viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());

        setCollapsingToolbarEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        getActivity().findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Glide.with(OwnProfileFragment.this)
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
    public void onResume() {
        super.onResume();
        setCollapsingToolbarEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        getActivity().findViewById(R.id.loading).setVisibility(View.VISIBLE);
        Glide.with(OwnProfileFragment.this)
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
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_own_profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        setCollapsingToolbarEnabled(false);
    }

    private void setCollapsingToolbarEnabled(boolean enabled){
        final Resources r = getResources();
        final int px = (int)(r.getDisplayMetrics().heightPixels / 2.5);
       CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar);

        if(enabled){
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            toolbar.setLayoutParams(params);

            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("");
            toolbar.setTitleEnabled(true);
            profilePic.setMaxHeight(px);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, false);
            toolbar.setTitle("Dein Profil");
            ((FloatingActionButton)getActivity().findViewById(R.id.fab2)).show();
        }
        else{
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            toolbar.setLayoutParams(params);

            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
            tabLayout.setVisibility(TabLayout.GONE);

            toolbar.setTitleEnabled(false);
            profilePic.setMaxHeight(0);
            profilePic.setImageBitmap(null);
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true, false);
            ((FloatingActionButton)getActivity().findViewById(R.id.fab2)).show();
        }
    }
}

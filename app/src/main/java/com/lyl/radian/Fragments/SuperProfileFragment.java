package com.lyl.radian.Fragments;


import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by Yannick on 10.11.2016.
 */

public class SuperProfileFragment extends Fragment {

    View view;
    RecyclerView bidList;
    public ArrayList<String[]> bieteItems = new ArrayList<String[]>();
    public RecyclerView.Adapter adapter;
    public Comparator<String[]> cmp;
    Account account;
    protected Handler mHandler;
    protected Boolean stop = false;
    protected Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            stop = false;
        }
    };

    public void expandToolbar() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinatorLayout);
        AppBarLayout appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.app_bar_layout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null && !stop)
        {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, -10000, false);
            stop = true;
            useHandler();
        }
    }

    public void useHandler()
    {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 200);
    }
}

package com.lyl.radian.Fragments;


import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;

import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by Yannick on 10.11.2016.
 */

public class SuperProfileFragment extends Fragment {

    View view;
    RecyclerView bidList;
    public ArrayList<Bid> bieteItems = new ArrayList<>();
    public RecyclerView.Adapter adapter;
    public Comparator<String[]> cmp;
    Account account;
}

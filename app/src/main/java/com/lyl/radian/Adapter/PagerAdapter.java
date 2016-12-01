package com.lyl.radian.Adapter;

/**
 * Created by Yannick on 25.11.2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lyl.radian.Fragments.OwnBidsFragment;
import com.lyl.radian.Fragments.UpcomingFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OwnBidsFragment tab1 = new OwnBidsFragment();
                return tab1;
            case 1:
                UpcomingFragment tab2 = new UpcomingFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
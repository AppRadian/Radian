package app.radiant.c.lly.Fragments;

import android.content.Intent;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.radiant.c.lly.Activities.MainAppActivity;
import app.radiant.c.lly.Activities.SettingsActivity;
import app.radiant.c.lly.Adapter.PagerAdapter;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;

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

        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);

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

        Resources r = getResources();
        int px = r.getDisplayMetrics().heightPixels / 3;

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        content.setLayoutParams(p);

        profilePic.setImageBitmap(ThumbnailUtils.extractThumbnail(account.getSelf().getProfilePic(), profilePic.getWidth(), (int)px));
        if(!isProfilePrevious())
            ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText("");
        else
            ((AppBarLayout)getActivity().findViewById(R.id.app_bar_layout)).setExpanded(true);
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(true);
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitle("Dein Profil");
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        ((MainAppActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_own_profile);
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
}

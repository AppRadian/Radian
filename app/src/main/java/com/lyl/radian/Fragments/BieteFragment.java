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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import com.lyl.radian.Activities.MainAppActivity;
import com.lyl.radian.Adapter.CustomRecyclerViewAdapterBiete;
import com.lyl.radian.Adapter.RecyclerItemClickListener;
import com.lyl.radian.DialogFragments.BidDialog;
import com.lyl.radian.DialogFragments.MyDialogCloseListener;
import com.lyl.radian.NetworkUtilities.LoadOwnBids;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;
import com.lyl.radian.Widgets.HidingScrollListener;

/**
 * Created by Yannick on 03.11.2016.
 */

public class BieteFragment extends Fragment implements MyDialogCloseListener {

    View view;
    MainAppActivity callingActivity;
    RecyclerView bieteList;
    public CustomRecyclerViewAdapterBiete adapter;
    Account account;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_biete, container, false);
        callingActivity = (MainAppActivity) getActivity();

        account = (Account) callingActivity.getApplication();

        adapter = new CustomRecyclerViewAdapterBiete(this, account.getSelf().getOwnBids());
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

                String id = adapter.getItem(position)[0];
                String email = adapter.getItem(position)[1];
                String tag = adapter.getItem(position)[2];
                String description = adapter.getItem(position)[3];
                String location = adapter.getItem(position)[4];
                String averageRating = adapter.getItem(position)[5];
                String count = adapter.getItem(position)[6];
                String date = adapter.getItem(position)[7];
                String time = adapter.getItem(position)[8];
                String part = adapter.getItem(position)[9];
                String maxPart = adapter.getItem(position)[10];

                account.setSearchedItem(getActivity(), id, email, tag, description, location, averageRating, count, null, date, time, part, maxPart);
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

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Deine Angebote");
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);

        HashMap<String, String> data = account.getAuthMap();
        data.put("email", account.getSelf().getEmail());
        data.put("lastId", Constants.lastIdOwnBids);
        new LoadOwnBids(this, data).execute();
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

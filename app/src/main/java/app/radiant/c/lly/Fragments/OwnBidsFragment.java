package app.radiant.c.lly.Fragments;

import android.content.res.Resources;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import app.radiant.c.lly.Adapter.CustomRecyclerViewAdapterOwnProfile;
import app.radiant.c.lly.Adapter.RecyclerItemClickListener;
import app.radiant.c.lly.NetworkUtilities.HomeShowBids;
import app.radiant.c.lly.NetworkUtilities.LoadOwnBids;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;
import app.radiant.c.lly.Widgets.NestedScrollViewFling;

/**
 * Created by Yannick on 03.11.2016.
 */

public class OwnBidsFragment extends SuperProfileFragment {

    ImageView profilePic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        account = (Account) getActivity().getApplication();

        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);
        adapter = new CustomRecyclerViewAdapterOwnProfile(this, account.getSelf().getOwnBids());
        bidList = (RecyclerView) view.findViewById(R.id.cardList);
        bidList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        bidList.setLayoutManager(llm);
        bidList.setAdapter(adapter);
        bidList.setNestedScrollingEnabled(false);
        ((NestedScrollViewFling)view.findViewById(R.id.nestedScrollView)).setOnTopReachedListener(new NestedScrollViewFling.OnFlingEndReachedTopListener()
        {
            @Override
            public void onTopReached(Boolean isBeingTouched)
            {
                if (!isBeingTouched)
                    expandToolbar();
            }
        });

        bidList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if(position == 0)
                            return;

                        CustomRecyclerViewAdapterOwnProfile adapter = (CustomRecyclerViewAdapterOwnProfile) OwnBidsFragment.this.adapter;

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
                        getChildFragmentManager().beginTransaction().replace(R.id.content_frame, f, "OwnsearchItem").addToBackStack(null).commit();
                    }
                })
        );

        return view;
    }

    private void refresh() {

        HashMap<String, String> data = account.getAuthMap();
        data.put("email", account.getSelf().getEmail());
        data.put("latitude", String.valueOf(account.getSelf().getLat()));
        data.put("longitude", String.valueOf(account.getSelf().getLng()));
        data.put("lastId", Constants.lastIdOwnBids);
        new LoadOwnBids(this, data).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
package app.radiant.c.lly.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Comparator;
import java.util.HashMap;

import app.radiant.c.lly.Activities.ChatActivity;
import app.radiant.c.lly.Adapter.CustomRecyclerViewAdapter;
import app.radiant.c.lly.Adapter.RecyclerItemClickListener;
import app.radiant.c.lly.NetworkUtilities.GetParticipations;
import app.radiant.c.lly.NetworkUtilities.LoadBids;
import app.radiant.c.lly.NetworkUtilities.SearchUser;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Widgets.NestedScrollViewFling;

/**
 * Created by len13 on 17.10.2016.
 */

public class ProfileFragment extends SuperProfileFragment {

    ImageView profilePic;
    FloatingActionButton sendMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        account =(Account) getActivity().getApplication();

        profilePic = (ImageView) getActivity().findViewById(R.id.ownProfilePic);

        adapter = new CustomRecyclerViewAdapter(getActivity(), bieteItems);
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

                        String id = adapter.getItem(position)[0];
                        String email = adapter.getItem(position)[1];
                        String tag = adapter.getItem(position)[2];
                        String description = adapter.getItem(position)[3];
                        String location = adapter.getItem(position)[4];
                        String averageRating = adapter.getItem(position)[5];
                        String count = adapter.getItem(position)[6];
                        String distance = adapter.getItem(position)[7];
                        String date = adapter.getItem(position)[8];
                        String time = adapter.getItem(position)[9];
                        String part = adapter.getItem(position)[10];
                        String maxPart = adapter.getItem(position)[11];

                        account.setSearchedItem(getActivity(), id, email, tag, description, location, averageRating, count, distance, date, time, part, maxPart);
                        SearchItemFragment f = new SearchItemFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack(null).commit();
                    }
                })
        );

        HashMap<String, String> data = account.getAuthMap();
        data.put("email", account.getSearchedItem().getEmail());
        data.put("latitude", String.valueOf(account.getSelf().getLat()));
        data.put("longitude", String.valueOf(account.getSelf().getLng()));
        new LoadBids(this, data).execute();

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);

        sendMessage = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        sendMessage.setVisibility(View.VISIBLE);
        sendMessage.setImageResource(R.drawable.ic_menu_send);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });


        Resources r = getResources();
        int px = r.getDisplayMetrics().heightPixels / 3;

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        content.setLayoutParams(p);

        profilePic.setImageBitmap(ThumbnailUtils.extractThumbnail(account.getBitmapFromCache(account.getSearchedItem().getEmail()), profilePic.getWidth(), (int)px));
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("");
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(true);
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitle(account.getSearchedItem().getEmail() + "'s Profil");

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

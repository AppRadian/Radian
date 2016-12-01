package app.radiant.c.lly.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import app.radiant.c.lly.Activities.SettingsActivity;
import app.radiant.c.lly.Adapter.CustomAdapterSearch;
import app.radiant.c.lly.NetworkUtilities.LoadOwnBids;
import app.radiant.c.lly.NetworkUtilities.SearchBid;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 03.11.2016.
 */

public class SearchFragment extends Fragment {

    View view;
    Activity callingActivity;
    EditText searchField;
    Button searchBtn;
    Account account;
    public ListView searches;
    public ArrayList<String[]> listItems = new ArrayList<String[]>();
    public CustomAdapterSearch adapter;
    public Comparator<String[]> cmp;

    public SearchFragment(){
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        callingActivity = getActivity();
        account = (Account) callingActivity.getApplication();

        searches = (ListView) view.findViewById(R.id.searchList);
        adapter = new CustomAdapterSearch(callingActivity, listItems);
        searches.setAdapter(adapter);

        searchField = (EditText) view.findViewById(R.id.editText3);
        searchField.setText(getArguments().getString("searchText"));

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);
        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);

        searchBtn = (Button) view.findViewById(R.id.searchBtn);

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

        final View.OnClickListener setLocation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setLocation = new Intent(getActivity(), SettingsActivity.class);
                startActivity(setLocation);
            }
        };
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.clear();
                adapter.notifyDataSetChanged();

                if(account.getSelf().getLocation().equals("N/A"))
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Please set your location first", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Set location", setLocation)
                            .setActionTextColor(Color.RED)
                            .show();
                else {
                    HashMap<String, String> data = account.getAuthMap();
                    data.put("tag", searchField.getText().toString());
                    data.put("latitude", String.valueOf(account.getSelf().getLat()));
                    data.put("longitude", String.valueOf(account.getSelf().getLng()));
                    new SearchBid(SearchFragment.this, data).execute();
                }
            }
        });

        searches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ID) {

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
                account.fm.beginTransaction().replace(R.id.content_frame, f, "searchItem").addToBackStack("searchItem").commit();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        getArguments().putString("searchText", searchField.getText().toString());
    }

    private void refresh(){

        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        content.setLayoutParams(p);

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Suche Angebote");
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}

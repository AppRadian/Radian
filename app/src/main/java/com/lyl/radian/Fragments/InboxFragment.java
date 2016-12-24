package com.lyl.radian.Fragments;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.lyl.radian.Activities.ChatActivity;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;


/**
 * Created by len13 on 11.11.2016.
 *
 * https://www.firebase.com/docs/android/quickstart.html
 */

public class InboxFragment extends Fragment {

    View view;
    Activity callingActivity;
    Account account;
    ListView chats;

    ArrayList<String> exampleContent = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private final String TAG = "InboxFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        callingActivity = getActivity();
        account = (Account) callingActivity.getApplication();

        // Code
        chats = (ListView) view.findViewById(R.id.userChatList);

        // Display name of persons you chat with
        Intent i = getActivity().getIntent();
        String chatPartner = i.getStringExtra("chatRoom"); // resolve error here : https://developer.android.com/training/basics/fragments/communicating.html
        Log.e(TAG, chatPartner);
        exampleContent.add("Len");
        /*
        Intent intentChatRoomExists = new Intent(getActivity(), ProfileFragment.class);
        if (exampleContent == null || exampleContent.contains(chatPartner)) {
            // send message to Profile Fragment that room exit
            intentChatRoomExists.putExtra("bool", "true");
        } else {
            // send message to Profile Fragment that room doesn't  exist
            intentChatRoomExists.putExtra("bool", "false");
            exampleContent.add(chatPartner);
        }*/

        // set adapter
        adapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_list_item_1, exampleContent);
        chats.setAdapter(adapter);

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public void refresh(){

        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Chats");
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}

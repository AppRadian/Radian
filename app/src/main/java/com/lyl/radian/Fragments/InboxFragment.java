package com.lyl.radian.Fragments;

import android.app.Activity;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Activities.ChatActivity;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;


/**
 * Created by len13 on 11.11.2016.
 *
 * https://www.firebase.com/docs/android/quickstart.html
 */

public class InboxFragment extends Fragment {

    // {BEGIN: Communication instances
    //OnChatRoomSelected mCallback;
    // END]

    View view;
    Activity callingActivity;
    Account account;
    ListView chats;

    FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();
    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        // Iterate through chats and display them
        DatabaseReference chatRef = firebaseDb.getReference("Users").child(user).child("myChats");
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exampleContent.removeAll(exampleContent);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    exampleContent.add(snapshot.getKey());
                }
                adapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_list_item_1, exampleContent);
                chats.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // set adapter
        //adapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_list_item_1, exampleContent);
        //chats.setAdapter(adapter);

        Log.e(TAG, String.valueOf(exampleContent.size()));

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //sendChatRoomName(String.valueOf(chats.getItemAtPosition(position)));
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
/*
    // [BEGIN: Interface for Fragment to Activity Communication
    public interface OnChatRoomSelected {
        public void selectedChatRoom(String chatRoomName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
       /* try {
            mCallback = (OnChatRoomSelected) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnChatRoomSelected");
        }
    }

    public void sendChatRoomName(String chatRoomName) {
        mCallback.selectedChatRoom(chatRoomName);
    }
    // END}
    */
}

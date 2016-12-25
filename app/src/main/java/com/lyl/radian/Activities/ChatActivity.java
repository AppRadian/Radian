package com.lyl.radian.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.DBObjects.Chat;
import com.lyl.radian.Fragments.InboxFragment;
import com.lyl.radian.R;

/**
 * Created by len13 on 19.11.2016.
 */

public class ChatActivity extends Activity /*implements InboxFragment.OnChatRoomSelected*/ {

    Button send;
    EditText msgField;
    ListView newsExtending;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference chats;

    Map<String, Object>  chatHistory;

    private final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messages = new ArrayList<>();
        send = (Button) findViewById(R.id.sendMsgBtn);
        msgField = (EditText) findViewById(R.id.aMessage);
        newsExtending = (ListView) findViewById(R.id.privateMessagesList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        newsExtending.setAdapter(adapter);

        // [BEGIN: Load messages
        database = FirebaseDatabase.getInstance();
        chats = database.getReference("Chats");
        chats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("-KZiHyjazb4A3za-bSme")) {
                        Chat aChat = dataSnapshot.getValue(Chat.class);
                        chatHistory = aChat.getMessages();

                        if (chatHistory == null) {
                            // No chats yet...
                            messages.add("No Chats yet");
                            adapter.notifyDataSetChanged();
                        } else {
                            for (Map.Entry<String, Object> entry : chatHistory.entrySet()) {
                                messages.add(entry.getKey() + entry.getValue().toString());
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // END]

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update DB
                final String text = msgField.getText().toString();
                chats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals("-KZiHyjazb4A3za-bSme")) {
                                Chat aChat = dataSnapshot.getValue(Chat.class);
                                aChat.addMessage("today", text);
                                DatabaseReference thisChat = FirebaseDatabase.getInstance().getReference("Chats").child("-KZiHyjazb4A3za-bSme");
                                thisChat.child("messages").setValue(aChat.getMessages());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                messages.add(text);
                adapter.notifyDataSetChanged();
                msgField.setText("");
            }
        });

    }
    /*
    @Override
    public void selectedChatRoom(String chatRoomName) {
        messages.add(chatRoomName);
        adapter.notifyDataSetChanged();
    }
    */
}

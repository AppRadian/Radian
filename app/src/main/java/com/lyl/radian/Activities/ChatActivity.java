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
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.DBObjects.Chat;
import com.lyl.radian.Interfaces.OnSelectedChatRoomCallback;
import com.lyl.radian.R;

/**
 * Created by len13 on 19.11.2016.
 */

public class ChatActivity extends Activity implements OnSelectedChatRoomCallback {

    Button send;
    EditText msgField;
    ListView newsExtending;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;

    String chatRoomName;

    private FirebaseDatabase database;
    private DatabaseReference chatsToRead;
    private DatabaseReference chatsToInsert;

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
        // TODO auslesen des keys und sagen wer welche Nachricht geschrieben hat
        // TODO Chat updaten also andere Instanzvariblen
        // TODO den richtigen Chatroom in diese Activity senden, den man ausgewählt hat
        // TODO ChatRoom beim ChatPartner einrichten
        database = FirebaseDatabase.getInstance();
        chatsToRead = database.getReference("Chats").child("-KZiHyjazb4A3za-bSme").child("messages");
        chatsToRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    messages.add((String) snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // END]

        chatsToInsert = database.getReference("Chats");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update DB
                final String text = msgField.getText().toString();
                chatsToInsert.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals("-KZiHyjazb4A3za-bSme")) {
                                Chat aChat = dataSnapshot.getValue(Chat.class);
                                Long tsLong = System.currentTimeMillis()/1000;
                                Map<String, Object> hm = aChat.getMessages();

                                if (hm == null) {
                                    hm = new HashMap<String, Object>();
                                    hm.put(tsLong.toString(), text);
                                }

                                aChat.setMessages(hm);
                                DatabaseReference thisChat = FirebaseDatabase.getInstance().getReference("Chats").child("-KZiHyjazb4A3za-bSme").child("messages");
                                thisChat.updateChildren(aChat.getMessages());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                messages.add(text);
                Log.e(TAG, chatRoomName);
                //messages.add(chatRoomName);
                adapter.notifyDataSetChanged();
                msgField.setText("");
            }
        });

    }

    @Override
    public void selectedChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
        Log.e(TAG, chatRoomName);
    }
}

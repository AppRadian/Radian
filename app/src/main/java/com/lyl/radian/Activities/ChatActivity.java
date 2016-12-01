package com.lyl.radian.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import com.lyl.radian.R;

/**
 * Created by len13 on 19.11.2016.
 */

public class ChatActivity extends Activity {

    Button send;
    EditText msgField;
    ListView newsExtending;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = msgField.getText().toString();
                messages.add(text);
                adapter.notifyDataSetChanged();
                msgField.setText("");
            }
        });

    }

}

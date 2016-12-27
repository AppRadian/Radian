package com.lyl.radian.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lyl.radian.Adapter.CustomAdapter;
import com.lyl.radian.DBObjects.Feedback;
import com.lyl.radian.DialogFragments.FeedbackDialog;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Constants.Constant;

/**
 * Created by Ludwig on 05.11.2016.
 */

public class ShowBidFeedbackActivity extends AppCompatActivity{

    TextView bid;
    FloatingActionButton rateBtn;
    ListView feedbackList;
    RatingBar ratingBar;
    public CustomAdapter adapter;
    public ArrayList<Feedback> feedbacks = new ArrayList<>();
    Account account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bid_feedback);

        account = (Account) getApplication();

        bid = (TextView) findViewById(R.id.bidType);
        rateBtn = (FloatingActionButton) findViewById(R.id.rateBtn);
        feedbackList = (ListView) findViewById(R.id.listFeedback);
        ratingBar = (RatingBar) findViewById(R.id.avergageRating);

        adapter = new CustomAdapter(this, feedbacks);
        feedbackList.setAdapter(adapter);

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackDialog add = new FeedbackDialog();
                add.setArguments(new Bundle());
                add.show(getSupportFragmentManager(), "Feedback Dialog");
            }
        });

        bid.setText(account.getClickedBid().getTag());
        ratingBar.setRating((float)account.getClickedBid().getAverageRating());

        DatabaseReference feedback = FirebaseDatabase.getInstance().getReference(Constant.FEEDBACK_DB).child(account.getClickedBid().getId());
        feedback.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Feedback feedback = dataSnapshot.getValue(Feedback.class);
                if(!feedbacks.contains(feedback))
                    feedbacks.add(feedback);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

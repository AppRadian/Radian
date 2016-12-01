package com.lyl.radian.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by len13 on 18.10.2016.
 */

public class FeedbackActivity extends Activity {

    RatingBar ratingBar;
    TextView feedback;
    TextView profileNameView;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        account = (Account) getApplication();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
        profileNameView = (TextView) findViewById(R.id.profileNameFeedback);
        feedback = (TextView) findViewById(R.id.textView5);

        Float rating = getIntent().getFloatExtra("rating", 0);
        String profileName = getIntent().getStringExtra("profileName");
        String feedbackText = getIntent().getStringExtra("feedbackText");

        ratingBar.setRating(rating);
        profileNameView.setText(profileName);
        feedback.setText(feedbackText);
    }
}

package com.lyl.radian.DialogFragments;

import android.app.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.DBObjects.Feedback;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;

import java.io.Console;

/**
 * Created by Yannick on 18.10.2016.
 */

public class FeedbackDialog extends DialogFragment {

    EditText feedback;
    RatingBar ratingBar;
    Button submit;
    Account account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.dialog_feedback, container, false);
        getDialog().setTitle("Share your feedback");
        account = (Account) getActivity().getApplication();

        feedback = (EditText) rootView.findViewById(R.id.feedbackText);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = ratingBar.getRating();
                String feedbackText = feedback.getText().toString();
                String fromUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Feedback feedbackToInsert = new Feedback(account.getClickedBid().getId(), fromUser, rating, feedbackText);

                DatabaseReference feedback = FirebaseDatabase.getInstance().getReference("Feedback");
                feedback.child(account.getClickedBid().getUserId()).setValue(feedbackToInsert);

                // Update the Bid from the user und in the bid DB
                // Get DB Reference of Bids
                DatabaseReference bids = FirebaseDatabase.getInstance().getReference("Bids");
                bids.child(account.getClickedBid().getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get the bid object from the DB
                        Bid bid = dataSnapshot.getValue(Bid.class);

                        //Extract the needed values
                        double averageRating = bid.getAverageRating();
                        long count = bid.getCount();
                        count++;

                        // set new count
                        bid.setCount(count);

                        // calculate new average rating
                        averageRating = ((averageRating * (count - 1)) + ratingBar.getRating()) / bid.getCount();
                        bid.setAverageRating(averageRating); // set new average rating in the object class

                        // Transfer updates to DB
                        DatabaseReference bids = FirebaseDatabase.getInstance().getReference(Constants.BID_DB);
                        bids.child(account.getClickedBid().getId()).setValue(bid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                // Update Ragin


                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Activity activity = getActivity();
        if(activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)activity).handleDialogClose(dialog);
    }
}

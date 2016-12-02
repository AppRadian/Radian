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

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

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
                float rating = ratingBar.getRating();
                String feedbackText = feedback.getText().toString();
                //account.addFeedback(FeedbackDialog.this, Integer.parseInt(account.getSearchedItem().getId()), account.getSearchedItem().getTag(), feedbackText, rating);
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

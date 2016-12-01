package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.HashMap;

import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 05.11.2016.
 */

public class AddFeedback extends AsyncTask <Void, Void, String>{

    Account account;
    DialogFragment callingDialog;
    RequestHandler rh = new RequestHandler();
    private String emailAuth;
    private String sessionId;
    private String email;
    private int id;
    private String tag;
    private String fromUser;
    private String text;
    private float rating;

    public AddFeedback(DialogFragment callingDialog, String emailAuth, String sessionId, int id, String tag, String fromUser, String text, float rating){

        account = (Account) callingDialog.getActivity().getApplication();
        this.callingDialog = callingDialog;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.email = email;
        this.id = id;
        this.tag = tag;
        this.fromUser = fromUser;
        this.text = text;
        this.rating = rating;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        HashMap<String,String> data = new HashMap<>();

        data.put("emailAuth", emailAuth);
        data.put("sessionId", sessionId);

        data.put("id", String.valueOf(id));
        data.put("tag", tag);
        data.put("fromUser", fromUser);
        data.put("text", text);
        data.put("rating", String.valueOf(rating));
        String result = rh.sendPostRequest(Constants.DBADDFEEDBACK,data);

        return result;
    }

    @Override
    protected void onPostExecute(final String result) {
        Activity callingActivity = callingDialog.getActivity();
        if(callingDialog != null)
            callingDialog.dismiss();
        if(result.equals("connection error")) Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AddFeedback(callingDialog, emailAuth, sessionId, id, tag, fromUser, text, rating).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }
}

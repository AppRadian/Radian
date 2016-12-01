package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 30.10.2016.
 */

public class UpdateFirebaseId extends AsyncTask<Void, Void, String > {

    Account account;
    ProgressDialog loading;
    private Activity callingActivity;
    private String emailAuth;
    private String sessionId;
    private String email;
    private String firebaseId;
    RequestHandler rh = new RequestHandler();

    public UpdateFirebaseId(Activity callingActivity, String emailAuth, String sessionId, String email, String firebaseId){

        this.account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.email = email;
        this.firebaseId = firebaseId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Uploading...", null,true,true);
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String,String> data = new HashMap<>();

        data.put("emailAuth", emailAuth);
        data.put("sessionId", sessionId);

        data.put("email", email);
        data.put("firebaseId", firebaseId);
        Log.e("firebase", "Hashmap: " + data.toString());
        String result = rh.sendPostRequest(Constants.DBFIREBASEID,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
        if(result.equals("connection error"))
            Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdateFirebaseId(callingActivity, emailAuth, sessionId, email, firebaseId).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }
}

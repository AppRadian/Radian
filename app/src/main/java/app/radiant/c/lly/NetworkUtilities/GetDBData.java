package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public abstract class GetDBData extends AsyncTask<Void, Void, String> {

    public Account account;
    public Activity callingActivity;
    private RequestHandler rh = new RequestHandler();
    private String url;

    public HashMap<String, String> data;

    public GetDBData(Activity callingActivity, String url, HashMap<String, String> data) {

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.url = url;
        this.data = data;
    }

    protected abstract void retry();

    protected abstract void parseJSON(String result);

    //You can implement it if you want a special functionality
    protected void noEntries(){

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        String result = rh.sendPostRequest(url, data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("connection error"))
            Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            retry();
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        if (result.equals("log in first"))
            account.logout(callingActivity);
        else {
            if (result.equals("No entries")) {
                Snackbar.make(callingActivity.findViewById(android.R.id.content), "No entries", Snackbar.LENGTH_SHORT)
                        .show();
                noEntries();
            }
            else {
                parseJSON(result);
            }
        }
    }
}
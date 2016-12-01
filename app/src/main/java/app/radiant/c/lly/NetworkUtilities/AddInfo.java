package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
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

public class AddInfo extends AsyncTask<Void, Void, String > {

    Account account;
    Activity callingActivity;
    String emailAuth;
    String sessionId;
    String email;
    String key;
    String value;
    String oldPw;
    RequestHandler rh = new RequestHandler();

    public AddInfo(Activity callingActivity, String emailAuth, String sessionId, String email, String key, String value){

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.email = email;
        this.key = key;
        this.value = value;
    }

    public AddInfo(Activity callingActivity, String emailAuth, String sessionId, String email, String key, String oldPw, String newPw){

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.email = email;
        this.key = key;
        this.oldPw = oldPw;
        this.value = newPw;
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

        data.put("email", email);
        data.put(key, value);
        String link = "";
        switch (key){
            case("password"):
                data.put("oldPw", oldPw);
                link = Constants.DBEDITPASSWORD;
                break;
            case("location"):
                link = Constants.DBEDITLOCATION;
                break;
            case("language"):
                link = Constants.DBEDITLANGUAGE;
                break;
        }
        String result = rh.sendPostRequest(link,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("connection error")) Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(key.equals("password"))
                            new AddInfo(callingActivity, emailAuth, sessionId, email, key, oldPw, value).execute();
                        else
                            new AddInfo(callingActivity, emailAuth, sessionId, email, key, value).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }
}

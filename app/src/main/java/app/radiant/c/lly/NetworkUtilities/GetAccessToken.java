package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import app.radiant.c.lly.Activities.MainAppActivity;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class GetAccessToken extends AsyncTask<Void, Void, String> {

    private Activity callingActivity;
    private String emailAuth;
    private String sessionId;
    private String email;
    RequestHandler rh = new RequestHandler();

    public GetAccessToken(Activity callingActivity, String emailAuth, String sessionId, String email) {

        this.callingActivity = callingActivity;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.email = email;
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
        String result = rh.sendPostRequest(Constants.DBGETACCESTOKEN,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("connection error"))
            Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new GetAccessToken(callingActivity, emailAuth, sessionId, email).execute();
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        else {
            if(result.equals("error"))
                Toast.makeText(callingActivity, "Couldnt get Access Token", Toast.LENGTH_LONG).show();
            else{
                SharedPreferences sp = callingActivity.getSharedPreferences("login_state", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", email);
                editor.putString("accessToken", result);
                editor.commit();
            }
        }
        Intent search = new Intent(callingActivity, MainAppActivity.class);
        callingActivity.startActivity(search);
        callingActivity.finishAffinity();
    }
}
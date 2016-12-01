package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import app.radiant.c.lly.Activities.FirebaseActivity;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 26.10.2016.
 */

public class Register extends AsyncTask<Void, Void, String>{

    ProgressDialog loading;
    Account account;
    private FirebaseActivity callingActivity;
    private String email;
    private String password;
    RequestHandler rh = new RequestHandler();

    public Register(FirebaseActivity callingActivity, String email, String password) {

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Uploading...", null,true,true);
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String,String> data = new HashMap<>();

        data.put("email", email);
        data.put("password", password);
        String result = rh.sendPostRequest(Constants.DBREGISTER,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
        if(result.equals("connection error")) Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Register(callingActivity, email, password).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
        else {
            if (result.equals("User added, logging in...") || result.equals("User already exists, logging in instead..."))
                account.login(callingActivity, email, password);
        }
    }
}

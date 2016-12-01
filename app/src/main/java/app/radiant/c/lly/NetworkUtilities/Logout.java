package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import app.radiant.c.lly.Activities.MainActivity;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class Logout extends AsyncTask<Void, Void, String> {

    ProgressDialog loading;
    private Account account;
    private Activity callingActivity;
    private String emailAuth;
    private String sessionId;
    RequestHandler rh = new RequestHandler();

    public Logout(Activity callingActivity, String emailAuth, String sessionId) {

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        // Den User von Firebase ausloggen wenn man sich aus der App abmeldet
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("Logout", user.getEmail() + " wurde ausgeloggt.");
            FirebaseAuth.getInstance().signOut();
        } else {
            Log.e("Logout", "kein User angemeldet");
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Logging out...", null,true,true);
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String,String> data = new HashMap<>();

        data.put("emailAuth", emailAuth);
        data.put("sessionId", sessionId);
        data.put("email", emailAuth);
        String result = rh.sendPostRequest(Constants.DBLOGOUT,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
        SharedPreferences sp = callingActivity.getSharedPreferences("login_state", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove("email");
        editor.remove("accessToken");
        editor.apply();
        setSelfInfo();

        Intent main = new Intent(callingActivity, MainActivity.class);
        callingActivity.startActivity(main);
        callingActivity.finishAffinity();
    }

    private void setSelfInfo(){

        account.deleteInfo();
    }
}
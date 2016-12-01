package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import app.radiant.c.lly.Activities.FirebaseActivity;
import app.radiant.c.lly.Activities.LoginActivity;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class Login extends AsyncTask<Void, Void, String> {

    ProgressDialog loading;
    private Account account;
    private FirebaseActivity callingActivity;
    private String email;
    private String password;
    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener userAuthListener;
    RequestHandler rh = new RequestHandler();

    public Login(FirebaseActivity callingActivity, String email, String password) {

        account = (Account) callingActivity.getApplication();
        this.callingActivity = callingActivity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Logging in...", null,true,true);
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String,String> data = new HashMap<>();

        data.put("email", email);
        data.put("password", password);
        String result = rh.sendPostRequest(Constants.DBLOGIN,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("connection error"))
            Snackbar.make(callingActivity.findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Login(callingActivity, email, password).execute();
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        else {
            if (result.equals("Password incorrect") || result.equals("User doesnt exist, please register"))
                Toast.makeText(callingActivity, result, Toast.LENGTH_SHORT).show();
            else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray user = jsonObj.getJSONArray("user");
                    JSONObject userInfo = user.getJSONObject(0);

                    String sessionId = userInfo.getString("sessionId");
                    String location = userInfo.getString("location");
                    String language = userInfo.getString("language");
                    String encodedPic = userInfo.getString("profilePic");

                    Resources r = callingActivity.getResources();
                    float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, r.getDisplayMetrics());
                    int width = r.getDisplayMetrics().widthPixels;

                    if(encodedPic.length() > 0) {
                        byte[] decodedString = Base64.decode(encodedPic, Base64.DEFAULT);
                        Bitmap decodedByte = Constants.decodeBitmap(decodedString, decodedString.length, width, (int)height);
                        setSelfInfo(sessionId, email, location, language, decodedByte);
                    }
                    else {
                        Bitmap bitmap = Constants.decodeBitmap(r, R.drawable.blank_profile_pic, width, (int)height);
                        setSelfInfo(sessionId, email, location, language, bitmap);
                    }
                    account.getAccessToken(callingActivity);

                    HashMap<String, String> data = account.getAuthMap();
                    data.put("email", email);
                    data.put("latitude", String.valueOf(account.getSelf().getLat()));
                    data.put("longitude", String.valueOf(account.getSelf().getLng()));
                    new GetParticipations(callingActivity, data).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(callingActivity, "Couldnt get User Info", Toast.LENGTH_LONG);
                }
            }
        }
        loading.dismiss();
    }

    private void setSelfInfo(String sessionId, String email, String location, String language, Bitmap profilePic) {

        account.setSessionId(sessionId);
        account.getSelf().setEmail(email);
        account.getSelf().setLocation(location);
        account.getSelf().setLanguage(language);
        account.getSelf().setProfilePic(profilePic);

        Double[] latLong = getLocationFromAddress(location);
        account.getSelf().setLat(latLong[0]);
        account.getSelf().setLng(latLong[1]);
    }

    public Double[] getLocationFromAddress(String strAddress){

        Double[] latLong = new Double[2];
        Geocoder coder = new Geocoder(callingActivity);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            latLong[0] = location.getLatitude();
            latLong[1] = location.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLong;
    }

    private void loginFirebase(){



    }
}
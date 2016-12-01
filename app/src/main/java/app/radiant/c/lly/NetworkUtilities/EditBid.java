package app.radiant.c.lly.NetworkUtilities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.HashMap;

import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;


/**
 * Created by Yannick on 30.10.2016.
 */

public class EditBid extends AsyncTask<Void, Void, String > {

    Account account;
    ProgressDialog loading;
    private Fragment callingFragment;
    private String emailAuth;
    private String sessionId;
    private String id;
    private String email;
    private String tag;
    private String description;
    private String location;
    double lat;
    double lng;
    String date;
    String time;
    String maxParticipators;
    RequestHandler rh = new RequestHandler();

    public EditBid(Fragment callingFragment, String emailAuth, String sessionId, String id, String email, String tag, String description, String location, double lat, double lng, String date, String time, String maxParticipators){

        this.account = (Account) callingFragment.getActivity().getApplication();
        this.callingFragment = callingFragment;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.id = id;
        this.email = email;
        this.tag = tag;
        this.description = description;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
        this.time = time;
        this.maxParticipators = maxParticipators;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingFragment.getActivity(), "Uploading...", null,true,true);
    }

    @Override
    protected String doInBackground(Void... params) {
        HashMap<String,String> data = new HashMap<>();

        data.put("emailAuth", emailAuth);
        data.put("sessionId", sessionId);

        data.put("id", id);
        data.put("email", email);
        data.put("tag", tag);
        data.put("description", description);
        data.put("location", location);
        data.put("latitude", String.valueOf(lat));
        data.put("longitude", String.valueOf(lng));
        data.put("date", date);
        data.put("time", time);
        data.put("maxParticipators", maxParticipators);
        String result = rh.sendPostRequest(Constants.DBEDITBID,data);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        loading.dismiss();
        if(result.equals("connection error"))
            Snackbar.make(callingFragment.getActivity().findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new EditBid(callingFragment, emailAuth, sessionId, id, email, tag, description, location, lat, lng, date, time, maxParticipators).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
        if(result.equals("Success"))
            for(int i = 0; i < account.getSelf().getOwnBids().size(); i++){
                String[] s = account.getSelf().getOwnBids().get(i);

                if(s[0].equals(id)){
                    account.getSelf().getOwnBids().remove(i);
                    account.getSelf().getOwnBids().add(i, new String[]{id, email, tag, description, location, String.valueOf(account.getSearchedItem().getAverageRating()), account.getSearchedItem().getCount(), date, time, account.getSearchedItem().getParticipators(), maxParticipators});
                }
            }
    }
}

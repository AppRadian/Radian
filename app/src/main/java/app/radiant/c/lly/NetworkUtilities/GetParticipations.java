package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.radiant.c.lly.Fragments.SuperProfileFragment;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class GetParticipations extends GetDBData{


    public GetParticipations(Activity callingActivity, HashMap<String, String> data){

        super(callingActivity, Constants.DBGETPARTICIPATIONS, data);
    }

    @Override
    protected void retry() {
        new GetParticipations(callingActivity, data).execute();
    }

    @Override
    protected void noEntries(){
    }

    @Override
    protected void parseJSON(String result) {
        try {
            ArrayList<String[]> l = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(result);
            JSONArray bids = jsonObj.getJSONArray("bids");

            for (int i = 0; i < bids.length(); i++) {
                JSONObject bidsInfo = bids.getJSONObject(i);

                String id = bidsInfo.getString("id");
                String email = bidsInfo.getString("email");
                String tag = bidsInfo.getString("tag");
                String description = bidsInfo.getString("description");
                String location = bidsInfo.getString("location");
                String avgRating = bidsInfo.getString("averageRating");
                String count = bidsInfo.getString("count");
                String distance = String.valueOf(Math.round(bidsInfo.getDouble("distance")));
                String date = bidsInfo.getString("date");
                String time = bidsInfo.getString("time");
                String part = bidsInfo.getString("part");
                String maxPart = bidsInfo.getString("maxPart");

                String[] arr = new String[]{id, email, tag, description, location, avgRating, count, distance, date, time, part, maxPart};
                l.add(arr);
            }
            account.getSelf().setParticipations(l);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(callingActivity, "Couldnt load Participations", Toast.LENGTH_LONG).show();
        }
    }
}

package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.radiant.c.lly.Activities.MainAppActivity;
import app.radiant.c.lly.Fragments.BieteFragment;
import app.radiant.c.lly.Fragments.OwnBidsFragment;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class LoadOwnBids extends GetDBData{

    private OwnBidsFragment ownBidsFragment;
    private BieteFragment bieteFragment;

    public LoadOwnBids(OwnBidsFragment ownBidsFragment, HashMap<String, String> data){

        super(ownBidsFragment.getActivity(), Constants.DBLOADOWNBID, data);
        this.ownBidsFragment = ownBidsFragment;
    }

    public LoadOwnBids(BieteFragment bieteFragment, HashMap<String, String> data){

        super(bieteFragment.getActivity(), Constants.DBLOADOWNBID, data);
        this.bieteFragment = bieteFragment;
    }

    @Override
    protected void retry() {
        if(bieteFragment != null)
            new LoadOwnBids(bieteFragment, data).execute();
        else if(ownBidsFragment != null)
            new LoadOwnBids(ownBidsFragment, data).execute();
    }

    @Override
    protected void parseJSON(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray bids = jsonObj.getJSONArray("bids");
            ArrayList<String[]> l = new ArrayList<>();
            for (int i = 0; i < bids.length(); i++) {
                JSONObject bidsInfo = bids.getJSONObject(i);

                String id = bidsInfo.getString("id");
                Constants.lastIdOwnBids = id;
                String email = bidsInfo.getString("email");
                String tag = bidsInfo.getString("tag");
                String description = bidsInfo.getString("description");
                String location = bidsInfo.getString("location");
                String avgRating = bidsInfo.getString("averageRating");
                String count = bidsInfo.getString("count");
                String date = bidsInfo.getString("date");
                String time = bidsInfo.getString("time");
                String part = bidsInfo.getString("part");
                String maxPart = bidsInfo.getString("maxPart");

                String[] arr = new String[]{id, email, tag, description, location, avgRating, count, date, time, part, maxPart};
                account.getSelf().getOwnBids().add(arr);
            }
            if(bieteFragment != null)
                bieteFragment.adapter.notifyDataSetChanged();
            else if(ownBidsFragment != null)
                ownBidsFragment.adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(callingActivity, "Couldnt load bids", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}

package app.radiant.c.lly.NetworkUtilities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import app.radiant.c.lly.Fragments.SearchFragment;
import app.radiant.c.lly.Fragments.SuperProfileFragment;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class LoadBids extends GetDBData{

    ProgressDialog loading;
    SuperProfileFragment callingFragment;

    public LoadBids(SuperProfileFragment callingFragment, HashMap<String, String> data){

        super(callingFragment.getActivity(), Constants.DBLOADBID, data);
        this.callingFragment = callingFragment;
    }

    @Override
    protected void retry() {

        new LoadBids(callingFragment, data).execute();
    }

    @Override
    protected void parseJSON(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray bids = jsonObj.getJSONArray("bids");
            callingFragment.bieteItems.clear();
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
                if (!callingFragment.bieteItems.contains(arr))
                    callingFragment.bieteItems.add(arr);
            }
            if(callingFragment.cmp != null)
                Collections.sort(callingFragment.bieteItems, callingFragment.cmp);
            callingFragment.adapter.notifyDataSetChanged();

            if (callingFragment.bieteItems.isEmpty())
                Snackbar.make(callingFragment.getActivity().findViewById(android.R.id.content), "No entries", Snackbar.LENGTH_SHORT)
                        .show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(callingFragment.getActivity(), "Couldnt load bids", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingFragment.getActivity(), "Uploading...", null,true,true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        loading.dismiss();
    }
}

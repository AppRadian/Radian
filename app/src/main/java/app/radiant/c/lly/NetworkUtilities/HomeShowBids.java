package app.radiant.c.lly.NetworkUtilities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import app.radiant.c.lly.Fragments.HomeFragment;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 29.10.2016.
 */

public class HomeShowBids extends GetDBData {

    ProgressDialog loading;
    HomeFragment callingFragment;

    public HomeShowBids(HomeFragment callingFragment, HashMap<String, String> data){

        super(callingFragment.getActivity(), Constants.DBHOMESHOWBIDS, data);
        this.callingFragment = callingFragment;
    }

    @Override
    protected void retry() {
        new HomeShowBids(callingFragment, data).execute();
    }

    @Override
    protected void parseJSON(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray bids = jsonObj.getJSONArray("bids");

            int max = 0;
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

                Constants.lastIdHome = id;

                String[] arr = new String[]{id, email, tag, description, location, avgRating, count, distance, date, time, part, maxPart};
                callingFragment.listItems.add(arr);
            }
            Collections.sort(callingFragment.listItems, callingFragment.cmp);
            callingFragment.adapter.notifyDataSetChanged();

            if (callingFragment.listItems.isEmpty())
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
        loading = ProgressDialog.show(callingFragment.getActivity(), "Suche Angebote in deiner Nähe...", null,true,true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callingFragment.swipeContainer.setRefreshing(false);
        loading.dismiss();
    }
}

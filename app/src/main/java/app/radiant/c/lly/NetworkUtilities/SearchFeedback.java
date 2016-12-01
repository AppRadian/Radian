package app.radiant.c.lly.NetworkUtilities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.radiant.c.lly.Activities.ShowBidFeedbackActivity;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 05.11.2016.
 */

public class SearchFeedback extends GetDBData{

    ProgressDialog loading;
    ShowBidFeedbackActivity callingActivity;

    public SearchFeedback(ShowBidFeedbackActivity callingActivity, HashMap<String, String> data){

        super(callingActivity, Constants.DBSEARCHFEEDBACK, data);
        this.callingActivity = callingActivity;
    }

    @Override
    protected void retry() {
        new SearchFeedback(callingActivity, data).execute();
    }

    @Override
    protected void parseJSON(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray feedback = jsonObj.getJSONArray("feedback");
            double averageRating = feedback.getJSONObject(0).getDouble("averageRating");
            callingActivity.setStars(averageRating);

            JSONArray allFeedback = feedback.getJSONObject(0).getJSONArray("allFeedback");
            callingActivity.feedbacks.clear();
            for (int i = 0; i < allFeedback.length(); i++) {
                JSONObject bidsInfo = allFeedback.getJSONObject(i);

                String fromUser = bidsInfo.getString("fromUser");
                String text = bidsInfo.getString("text");
                String rating = bidsInfo.getString("rating");

                String[] arr = new String[]{fromUser, text, rating};
                callingActivity.feedbacks.add(arr);
            }
            callingActivity.adapter.notifyDataSetChanged();

            if (callingActivity.feedbacks.isEmpty())
                Snackbar.make(callingActivity.findViewById(android.R.id.content), "No entries", Snackbar.LENGTH_SHORT)
                        .show();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("test", "result " + result);
            Toast.makeText(callingActivity, "Couldnt load feedback", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Uploading...", null,true,true);
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        loading.dismiss();
    }
}

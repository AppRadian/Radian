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

import java.util.HashMap;

import app.radiant.c.lly.Adapter.CustomRecyclerViewAdapter;
import app.radiant.c.lly.Fragments.ProfileFragment;
import app.radiant.c.lly.Fragments.SearchItemFragment;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 05.11.2016.
 */

public class SearchUser extends GetDBData{


    SearchItemFragment callingFragment;
    ProgressDialog loading;

    public SearchUser(SearchItemFragment callingFragment, HashMap<String, String> data){

        super(callingFragment.getActivity(), Constants.DBSEARCHUSER, data);
        this.callingFragment = callingFragment;
    }

    @Override
    protected void retry() {
        new SearchUser(callingFragment, data).execute();
    }

    @Override
    protected void parseJSON(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray user = jsonObj.getJSONArray("user");
            JSONObject userInfo = user.getJSONObject(0);

            String location = userInfo.getString("location");
            String language = userInfo.getString("language");

            account.setSearchedUser(location, language);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(callingFragment.getActivity(), "Couldnt get User Info", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingFragment.getActivity(), "Loading profile...", null,true,true);
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        loading.dismiss();
        ProfileFragment f = new ProfileFragment();
        account.fm.beginTransaction().replace(R.id.content_frame, f, "profile").addToBackStack("profile").commit();
    }
}

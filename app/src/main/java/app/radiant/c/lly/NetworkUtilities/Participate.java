package app.radiant.c.lly.NetworkUtilities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.HashMap;

import app.radiant.c.lly.Fragments.SearchItemFragment;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 30.10.2016.
 */

public class Participate extends AsyncTask<Void, Void, String > {

    ProgressDialog loading;
    private Account account;
    private SearchItemFragment callingFragment;
    private String emailAuth;
    private String sessionId;
    private String bidId;
    private String email;
    RequestHandler rh = new RequestHandler();

    public Participate(SearchItemFragment callingFragment, String emailAuth, String sessionId, String bidId, String email){

        account = (Account)callingFragment.getActivity().getApplication();
        this.callingFragment = callingFragment;
        this.emailAuth = emailAuth;
        this.sessionId = sessionId;
        this.bidId = bidId;
        this.email = email;

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

        data.put("bidId", bidId);
        data.put("email", email);
        String result = rh.sendPostRequest(Constants.DBPARTICIPATE,data);

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
                        new Participate(callingFragment, emailAuth, sessionId, bidId, email).execute();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
        else if(result.equals("Success")){
            account.getSelf().getParticipations().add(account.getSearchedItem().getArray());
            callingFragment.setButtonText("Nicht mehr teilnehmen");
        }
        else
            Snackbar.make(callingFragment.getActivity().findViewById(android.R.id.content), result, Snackbar.LENGTH_LONG).show();
    }
}

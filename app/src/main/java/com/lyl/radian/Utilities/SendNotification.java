package com.lyl.radian.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Yannick on 08.12.2016.
 */

public class SendNotification extends AsyncTask<Object, Object, String> {

    String registrationId;
    String msg;
    public SendNotification(String registrationId, String msg){
        this.registrationId = registrationId;
        this.msg = msg;
    }

    @Override
    protected String doInBackground(Object... objects) {
        RequestHandler rh = new RequestHandler();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("registration_id", registrationId);
        hm.put("data.msg", msg);
        String resp = rh.sendPostRequest("https://fcm.googleapis.com/fcm/send", hm);
        return resp;
    }
}

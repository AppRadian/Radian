package app.radiant.c.lly.NetworkUtilities;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

/**
 * Created by Yannick on 23.11.2016.
 */

public class GetBitmap extends AsyncTask<Void, Void, Bitmap> {

    Account account;
    Activity context;
    ImageView imageView;
    String email;

    public GetBitmap(Activity context, ImageView imageView, String email){

        this.account = (Account) context.getApplication();
        this.context = context;
        this.imageView = imageView;
        this.email = email;
    }
    @Override
    protected Bitmap doInBackground(Void... params) {

        URL url = null;
        try {
            url = new URL(Constants.PICS + email + ".png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Resources r = context.getResources();
        int height = r.getDisplayMetrics().heightPixels / 3;
        int width = r.getDisplayMetrics().widthPixels;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(url.openStream(), null, options);
        } catch (IOException e) {
            //e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = Constants.calculateInSampleSize(options, width, (int)height);
        options.inPreferredConfig=Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap decodedByte = null;
        try {
            decodedByte = BitmapFactory.decodeStream(url.openStream(), null, options);
        } catch (IOException e) {
            //e.printStackTrace();
            decodedByte = Constants.decodeBitmap(r, R.drawable.blank_profile_pic, width, (int)height);
        }

        account.addBitmapToCache(email, decodedByte);

        return decodedByte;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);
    }
}

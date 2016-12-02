package com.lyl.radian.Utilities;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Yannick on 28.10.2016.
 */

public class Constants {

    // Database References
    public static final String BID_DB = "Bids";
    public static final String FEEDBACK_DB = "Feedback";
    public static final String USER_DB = "Users";

    // Auto Fill Location Name Google API Key String Constant
    public static final String PRIVATE_GOOGLE_API_KEY = "AIzaSyCDLrf4wg0oCpzYepocnaBMoXvoYws1cpw";
    public static final String PRIVATE_GOOGLE_FIREBASE_API_KEY = "AIzaSyCpfiikIRLYxFAoP_VIEXWElB4DiW-CLLU";
    public static final String DEFAULTPASSWORD = "emptyPassword1";
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int stretch_width = Math.round((float)width / (float)reqWidth);
        int stretch_height = Math.round((float)height / (float)reqHeight);

        if (stretch_width <= stretch_height)
            return stretch_height;
        else
            return stretch_width;
    }

    public static Bitmap decodeBitmap(byte[] pic, int length,
                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(pic, 0, length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig=Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(pic, 0, length, options);
    }

    public static Bitmap decodeBitmap(Activity activity, Uri uri,
                                      int reqWidth, int reqHeight) throws FileNotFoundException {

        InputStream input = activity.getContentResolver().openInputStream(uri);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig=Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        InputStream input2 = activity.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(input2, null, options);
    }

    public static Bitmap decodeBitmap(Resources res, int resId,
                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig=Bitmap.Config.RGB_565;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}

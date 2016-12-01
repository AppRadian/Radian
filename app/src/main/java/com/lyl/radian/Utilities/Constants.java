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

    public static String lastIdHome = "-1";
    public static String lastIdOwnBids = "-1";

    public static final String DBREGISTER = "http://change-together.spdns.org/app/register.php";
    public static final String DBLOGIN = "http://change-together.spdns.org/app/login.php";
    public static final String DBLOGOUT = "http://change-together.spdns.org/app/logout.php";
    public static final String DBGETACCESTOKEN = "http://change-together.spdns.org/app/getAccessToken.php";
    public static final String DBLOGINWITHACCESSTOKEN = "http://change-together.spdns.org/app/loginWithAccessToken.php";
    public static final String DBFIREBASEID = "http://change-together.spdns.org/app/updateFirebaseId.php";

    public static final String DBHOMESHOWBIDS = "http://change-together.spdns.org/app/homeShowBids.php";
    public static final String DBSEARCHBID = "http://change-together.spdns.org/app/searchBid.php";
    public static final String DBADDBID = "http://change-together.spdns.org/app/addBid.php";
    public static final String DBEDITBID = "http://change-together.spdns.org/app/editBid.php";
    public static final String DBLOADBID = "http://change-together.spdns.org/app/loadBids.php";
    public static final String DBLOADOWNBID = "http://change-together.spdns.org/app/loadOwnBids.php";
    public static final String DBDELETEBID = "http://change-together.spdns.org/app/deleteBid.php";
    public static final String DBPARTICIPATE = "http://change-together.spdns.org/app/participate.php";
    public static final String DBGETPARTICIPATIONS = "http://change-together.spdns.org/app/getParticipations.php";

    public static final String DBUPLOADPIC = "http://change-together.spdns.org/app/uploadPic.php";
    public static final String DBSHOWPIC = "http://change-together.spdns.org/app/showPic.php";
    public static final String DBEDITPASSWORD = "http://change-together.spdns.org/app/editPassword.php";
    public static final String DBEDITLOCATION = "http://change-together.spdns.org/app/editLocation.php";
    public static final String DBEDITLANGUAGE = "http://change-together.spdns.org/app/editLanguage.php";
    public static final String DBSEARCHUSER = "http://change-together.spdns.org/app/searchUser.php";
    public static final String DBSEARCHFEEDBACK = "http://change-together.spdns.org/app/searchFeedback.php";
    public static final String DBADDFEEDBACK = "http://change-together.spdns.org/app/addFeedback.php";

    public static final String PICS = "http://change-together.spdns.org/app/pics/";

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

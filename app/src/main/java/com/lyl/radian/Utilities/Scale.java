package com.lyl.radian.Utilities;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.squareup.picasso.Transformation;


/**
 * Created by Yannick on 14.11.2016.
 */

public class Scale implements Transformation {

    int mSize;
    boolean isHeightScale;
    float px;

    public Scale(int size, float px) {
        mSize = size;
        this.isHeightScale = false;
        this.px = px;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        float scale;
        int newSize;
        Bitmap scaleBitmap;
        Bitmap bitmap = null;
        if (isHeightScale) {
            scale = (float) mSize / source.getHeight();
            newSize = Math.round(source.getWidth() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, newSize, mSize, true);
        } else {
            scale = (float) mSize / source.getWidth();
            newSize = Math.round(source.getHeight() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, mSize, (int)px, true);

            bitmap = ThumbnailUtils.extractThumbnail(source, mSize, (int)px);
        }
        if (scaleBitmap != source) {
            source.recycle();
        }

        return bitmap;
    }
    @Override
    public String key() {
        return "scaleRespectRatio"+mSize+isHeightScale;
    }
}

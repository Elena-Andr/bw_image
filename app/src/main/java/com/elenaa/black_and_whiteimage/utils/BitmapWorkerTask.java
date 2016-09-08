package com.elenaa.black_and_whiteimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.elenaa.black_and_whiteimage.MainActivity;
import com.elenaa.black_and_whiteimage.processor.BradleyBinarize;
import com.elenaa.black_and_whiteimage.processor.FiltersEnum;
import com.elenaa.black_and_whiteimage.processor.OtsuBinarize;
import com.elenaa.black_and_whiteimage.processor.Sepia;

import java.io.IOException;

public class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {

    private static final String LOG_TAG = BitmapWorkerTask.class.getSimpleName();

    //Image view to which the resulted bitmap should be loaded
    private ImageView mImageView;

    //Filter to be applied (by default no filter is applied)
    private FiltersEnum mFilter = FiltersEnum.None;

    private Context mContext;

    //Specifies the rect of the bitmap which should be loaded to image view; can be null, in this case the whole bitmap is loaded
    private Rect mImageRect;

    public BitmapWorkerTask(Context context, ImageView imageView) {
        mImageView = imageView;
        mContext = context;
        mImageRect = null;
    }

    public BitmapWorkerTask(Context context, ImageView imageView, Rect imageRect) {
        mImageView = imageView;
        mContext = context;
        mImageRect = imageRect;
    }

    public void setFilter(FiltersEnum filter){
        mFilter = filter;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        Bitmap bitmap = null;

        try {
            // If rect is specified, load only the part of the image,
            // otherwise load the whole image
            if(mImageRect == null)
                bitmap = BitmapHelper.loadBitmapSafely(mContext, params[0]);
            else
                bitmap = BitmapHelper.loadBitmapSafely(mContext, params[0], mImageRect);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        // Apply filter if it's specified
        if(mFilter != FiltersEnum.None){
            switch (mFilter){
                case Otsu_Algorithm:
                    return OtsuBinarize.convertToBW(bitmap);
                case Sepia:
                    return Sepia.convertToSepia(bitmap);
                case Adaptive_binarization:
                    return BradleyBinarize.convertToBW(bitmap);
                default:
                    return bitmap;
            }
        }

        return bitmap;
    }

    // Set bitmap to the image view
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mImageView != null && bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}

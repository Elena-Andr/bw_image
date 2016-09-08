package com.elenaa.black_and_whiteimage.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class BitmapHelper {

    //This method is used for getting rid of OUM issue when loading bitmaps
    public static Bitmap loadBitmapSafely(Context context, Uri imageUri) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, onlyBoundsOptions);
        inputStream.close();

		//Open input stream once again to decode the bitmap
        inputStream = context.getContentResolver().openInputStream(imageUri);
        final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = false;

        // Calculate target size in accordance to the device width & height
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        bitmapOptions.inSampleSize = calculateInSampleSize(onlyBoundsOptions, screenWidth, screenHeight);

        // Decode bitmap with inSampleSize set
        Bitmap scaledBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        inputStream.close();

        return scaledBitmap;
    }

	//This method is used for getting rid of OUM issue when loading the specified region of bitmap
    public static Bitmap loadBitmapSafely(Context context, Uri imageUri, Rect imageRect) throws IOException {

        InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, onlyBoundsOptions);
        inputStream.close();

        inputStream = context.getContentResolver().openInputStream(imageUri);
        final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = false;

        // Calculate target size in accordance to the device width & height
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        bitmapOptions.inSampleSize = calculateInSampleSize(onlyBoundsOptions, screenWidth, screenHeight);

		//Decode the region of the input image
        BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        Bitmap scaledBitmap = bitmapRegionDecoder.decodeRegion(imageRect, bitmapOptions);
        inputStream.close();

        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Get height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

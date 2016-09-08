package com.elenaa.black_and_whiteimage.processor;

import android.graphics.Bitmap;
import android.graphics.Color;

public class OtsuBinarize {

    private static final String LOG_TAG = OtsuBinarize.class.getSimpleName();

    public static Bitmap convertToBW(Bitmap inputBitmap){

        if(inputBitmap == null){
            throw new IllegalArgumentException(LOG_TAG + ": input bitmap is null");
        }

        // First we convert image to grayscale
        Bitmap grayscaleBitmap = convertToGray(inputBitmap);

        // Binarize the resulted grayscale image
        Bitmap bwBitmap = binarize(grayscaleBitmap);

        return bwBitmap;
    }

    private static Bitmap binarize(Bitmap inputBitmap) {

        int red, alpha;
        int newPixel;

        int threshold = getOtsuThreshold(inputBitmap);

        Bitmap binarizedBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());

        for(int i = 0; i < inputBitmap.getWidth(); i++) {
            for(int j = 0; j < inputBitmap.getHeight(); j++) {
                
                int pixel = inputBitmap.getPixel(i, j);

                // Get pixels
                red = Color.red(pixel);
                alpha = Color.alpha(pixel);
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarizedBitmap.setPixel(i, j, newPixel);

            }
        }

        return binarizedBitmap;
    }

    // Get binary threshold using Otsu's method
    private static int getOtsuThreshold(Bitmap inputBitmap) {

        int[] histogram = getImageHistogram(inputBitmap);
        int total = inputBitmap.getHeight() * inputBitmap.getWidth();

        float sum = 0;
        for(int i = 0; i < 256; i++){
            sum += i * histogram[i];
        }

        float sumB = 0;
        int weightBackground = 0;
        int weightForeground = 0;

        float varMax = 0;
        int threshold = 0;

        for(int i=0 ; i < 256 ; i++) {
            weightBackground += histogram[i];
            if(weightBackground == 0) continue;
            weightForeground = total - weightBackground;

            if(weightForeground == 0) break;

            sumB += (float) (i * histogram[i]);
            float meanBackground = sumB / weightBackground;
            float meanForeground = (sum - sumB) / weightForeground;

            float varBetween = (float) weightBackground * (float) weightForeground * (meanBackground - meanForeground) * (meanBackground - meanForeground);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private static int[] getImageHistogram(Bitmap inputBitmap) {
        int[] histogram = new int[256];

        for(int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for(int i = 0; i < inputBitmap.getWidth(); i++) {
            for(int j = 0; j < inputBitmap.getHeight(); j++) {
                int pixel = inputBitmap.getPixel(i, j);
                int red = Color.red(pixel);
                histogram[red]++;
            }
        }

        return histogram;
    }

    // The luminance method
    private static Bitmap convertToGray(Bitmap original) {

        int alpha, red, green, blue;
        int newPixel;

        Bitmap lum = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        for(int i = 0; i < original.getWidth(); i++) {
            for(int j = 0; j < original.getHeight(); j++) {

                int pixel = original.getPixel(i, j);

                // Get pixels by R, G, B
                alpha = Color.alpha(pixel);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);

                red = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);

                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);

                // Write pixels into image
                lum.setPixel(i, j, newPixel);
            }
        }
        return lum;
    }

    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;
    }
}

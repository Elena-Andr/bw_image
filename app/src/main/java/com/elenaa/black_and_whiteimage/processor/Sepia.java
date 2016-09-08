package com.elenaa.black_and_whiteimage.processor;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Sepia {

    private static final String LOG_TAG = Sepia.class.getSimpleName();

    private static final int SEPIA_DEPTH = 20;
    private static final int SEPIA_INTENSITY = 10;

    public static Bitmap convertToSepia(Bitmap inputBitmap) {

        if(inputBitmap == null){
            throw new IllegalArgumentException(LOG_TAG + ": input bitmap is null");
        }

        final int width = inputBitmap.getWidth();
        final int height = inputBitmap.getHeight();

        Bitmap sepiaBitmap = Bitmap.createBitmap(width, height, inputBitmap.getConfig());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int pixel = inputBitmap.getPixel(i, j);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int gry = (red + green + blue) / 3;

                red = green = blue = gry;
                red = red + (SEPIA_DEPTH * 2);
                green = green + SEPIA_DEPTH;

                // Normalize if out of bounds
                if (red > 255) {
                    red = 255;
                }
                if (green > 255) {
                    green = 255;
                }
                if (blue > 255) {
                    blue = 255;
                }

                // Darken blue color to increase sepia effect
                blue -= SEPIA_INTENSITY;

                if (blue < 0) {
                    blue = 0;
                }
                if (blue > 255) {
                    blue = 255;
                }

                int newPixel = Color.argb(Color.alpha(pixel), red, green, blue);
                sepiaBitmap.setPixel(i, j, newPixel);

            }
        }
        return sepiaBitmap;
    }
}

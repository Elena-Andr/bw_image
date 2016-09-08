package com.elenaa.black_and_whiteimage.processor;


import android.graphics.Bitmap;
import android.graphics.Color;

public class BradleyBinarize {
    private static final String LOG_TAG = BradleyBinarize.class.getSimpleName();
    private static final float THRESHOLD = 0.15f;
    private static final int WINDOW_FACTOR = 8;

    public static Bitmap convertToBW(Bitmap inputBitmap) {

        if(inputBitmap == null){
            throw new IllegalArgumentException(LOG_TAG + ": input bitmap is null");
        }

        final int width = inputBitmap.getWidth();
        final int height = inputBitmap.getHeight();

        int integralImg[] = new int[width * height];

        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int sum, index;

        // First pass for computing the integral image.
        for (int i = 0; i < width; i++) {
            sum = 0;
            for (int j = 0; j < height; j++) {
                index = j * width + i;
                int pixel = inputBitmap.getPixel(i, j);
                int grayPixel = gray(pixel);
                sum += grayPixel;
                integralImg[index] = sum + ((i != 0) ? integralImg[index - 1] : 0);
            }
        }

        int x1, y1, x2, y2;
        int s2 = (width / WINDOW_FACTOR) / 2;

        // Second pass to perform thresholding
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                // Calculating corner positions for S X S box
                x1 = i - s2;
                x2 = i + s2;
                y1 = j - s2;
                y2 = j + s2;

                // Truncating border values
                if (x1 < 0) x1 = 0;
                if (x2 >= width) x2 = width - 1;
                if (y1 < 0) y1 = 0;
                if (y2 >= height) y2 = height - 1;

                // Total number of pixels in the box after truncation
                int count = (x2 - x1) * (y2 - y1);

                // Calculating sum over the box using integral image
                sum = integralImg[y2 * width + x2]
                        - integralImg[y1 * width + x2]
                        - integralImg[y2 * width + x1]
                        + integralImg[y1 * width + x1];

                // Binarizing the image based on threshold for the box
                if ((gray(inputBitmap.getPixel(i, j)) * count) < (long) (sum * (1.0 - THRESHOLD)))
                    outputBitmap.setPixel(i, j, Color.BLACK);
                else
                    outputBitmap.setPixel(i, j, Color.WHITE);
            }
        }

        return outputBitmap;
    }

    // Method to obtain gray pixel (standard luminance calculation)
    private static int gray(int pixel) {
        return (int) (0.2126 * Color.red(pixel) + 0.7152 * Color.green(pixel) + 0.0722 * Color.blue(pixel));
    }
}

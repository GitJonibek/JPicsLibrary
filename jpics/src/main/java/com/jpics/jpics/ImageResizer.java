package com.jpics.jpics;

import android.graphics.Bitmap;

public class ImageResizer{

    // - Used only static image sizes because of null pointer exception on getting bitmap.getWidth() & bitmap.getHeight();
    // - We are trying to get width of unscaled bitmap and it may cause NullPointerException.
    // - that's why I'm getting actual sizes. getWidth() = 64; getHeight() = 64;
    public static Bitmap ScaleDownBitmap(Bitmap originalImage, float maxImageSize, boolean filter){
        float ratio = Math.min( maxImageSize / 64, maxImageSize / 64);
        int width = Math.round(ratio * (float)64);
        int height = Math.round(ratio * (float)64);

        if(ratio >= 1)
            return originalImage;

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage, width, height, filter);
        return newBitmap;
    }

}

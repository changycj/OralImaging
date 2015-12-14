package edu.mit.media.cameraculture.oralimaging.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by judychang on 11/30/15.
 */
public class Util {

    public static Bitmap rotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}

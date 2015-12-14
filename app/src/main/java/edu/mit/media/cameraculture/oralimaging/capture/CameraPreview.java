package edu.mit.media.cameraculture.oralimaging.capture;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by judychang on 11/16/15.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mCamera = camera;
    }

    public void onResume(Camera camera) {
        Log.d(TAG, "onResume");
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        mHolder.removeCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        try{
            mCamera.startPreview();
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);

        } catch(IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");

        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error stopping camera preview: " + e.getMessage());
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
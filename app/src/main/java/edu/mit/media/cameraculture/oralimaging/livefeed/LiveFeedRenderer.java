package edu.mit.media.cameraculture.oralimaging.livefeed;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.mit.media.cameraculture.oralimaging.R;

/**
 * Created by judychang on 11/8/15.
 */
public class LiveFeedRenderer implements
        CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener{

    private static final String TAG = "LiveFeedRenderer";

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private Camera mCamera;
    private LiveFeedView mView;
    private SurfaceTexture mSurface;
    private LiveFeedOverlayView mOverlayView;

    private int texture;

    public LiveFeedRenderer(LiveFeedView view, Camera c) {
        mCamera = c;
        mView = view;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Log.i(TAG, "onNewFrame");
        Log.i(TAG, mSurface.toString());

        float[] mtx = new float[16];
        mSurface.updateTexImage();
        mSurface.getTransformMatrix(mtx);
    }

    @Override
    public void onDrawEye(Eye eye) {
        Log.i(TAG, "onDrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Log.i(TAG, "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");

        // start camera
        texture = createTexture();
        mSurface = new SurfaceTexture(texture);
        mSurface.setOnFrameAvailableListener(this);

        try
        {
            mCamera.setPreviewTexture(mSurface);
            mCamera.startPreview();
        }
        catch (IOException ioe)
        {
            Log.w("MainActivity","CAM LAUNCH FAILED");
        }
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "onFrameAvailable");
        mView.requestRender();
    }

    private int createTexture() {
        int[] t = new int[1];
        GLES20.glGenTextures(1, t, 0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, t[0]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return t[0];
    }
}

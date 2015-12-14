package edu.mit.media.cameraculture.oralimaging.capture;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.mit.media.cameraculture.oralimaging.MainActivity;
import edu.mit.media.cameraculture.oralimaging.R;
import edu.mit.media.cameraculture.oralimaging.livefeed.LiveFeedActivity;
import edu.mit.media.cameraculture.oralimaging.util.Util;

/**
 * Created by judychang on 11/28/15.
 */
public class CaptureActivity extends AppCompatActivity {
    private static final String TAG = "CaptureActivity";

    private Camera mCamera;
    private Camera.PictureCallback mPicture;
    private CameraPreview mPreview;

    private int selectedOrientation;

    // top left, bottom left, top right, bottom right
    private byte[][] pictureSet = new byte[][] { null, null, null, null };
    private int[] buttonIds = new int[] {
            R.id.capture_top_left,
            R.id.capture_top_right,
            R.id.capture_bottom_left,
            R.id.capture_bottom_right,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture);

        mCamera = getCamera();
        FrameLayout preview = (FrameLayout) findViewById(R.id.capture_preview);
        mPreview = new CameraPreview(getBaseContext(), mCamera);
        preview.addView(mPreview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.capture_toolbar);
        setSupportActionBar(toolbar);

        CaptureOrientationListener listener = new CaptureOrientationListener(buttonIds);

        selectedOrientation = 0;
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(buttonIds[selectedOrientation]).callOnClick();

        findViewById(R.id.capture_take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPicture);
            }
        });

        mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                for (int i = 0; i < buttonIds.length; i++) {
                    int id = buttonIds[i];
                    if (i == selectedOrientation) {
                        pictureSet[i] = data;

                        ((ImageButton) findViewById(id)).setImageBitmap(
                                Util.rotateBitmap(
                                        BitmapFactory.decodeByteArray(data, 0, data.length),
                                        90));
                        break;
                    }
                }

                selectedOrientation = (selectedOrientation + 1) % buttonIds.length;
                findViewById(buttonIds[selectedOrientation]).callOnClick();

                mCamera.startPreview();
            }
        };

        findViewById(R.id.capture_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File storageDir = getStorageDir();

                if (storageDir != null) {
                    for (int i = 0; i < pictureSet.length; i++) {
                        byte[] data = pictureSet[i];
                        String pictureFolderName;
                        switch (i) {
                            case 0:
                                pictureFolderName = "Top Left";
                                break;
                            case 1:
                                pictureFolderName = "Top Right";
                                break;
                            case 2:
                                pictureFolderName = "Bottom Left";
                                break;
                            case 3:
                                pictureFolderName = "Bottom Right";
                                break;
                            default:
                                pictureFolderName = "Unknown";
                        }
                        File pictureFolder = new File(storageDir, pictureFolderName);
                        pictureFolder.mkdirs();

                        File originalPicture = new File(pictureFolder, "original.bmp");

                        if (originalPicture != null && data != null) {
                            try {

                                // TODO: ROTATE IMAGE
                                FileOutputStream fos = new FileOutputStream(originalPicture);
                                fos.write(data);
                                fos.close();

                                Log.d(TAG, "File written: " + originalPicture.getPath());

                            } catch (FileNotFoundException e) {
                                Log.d(TAG, "File not found: " + originalPicture.getPath());

                            } catch (IOException e) {
                                Log.d(TAG, "File could not be written: " + originalPicture.getPath());
                            }
                        }
                    }
                }
                returnToMain();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.capture_actions, menu);
        return true;
    }

    private void returnToMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(myIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.capture_action_browse :
                returnToMain();
                break;

            default :
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mCamera == null) {
            mCamera = getCamera();
            mPreview.onResume(mCamera);
        }

        selectedOrientation = 0;
        for (int i = 0; i < pictureSet.length; i++) {
            pictureSet[i] = null;
        }

        for (int id : buttonIds) {
            ((ImageButton) findViewById(id)).setImageBitmap(null);

        }
        findViewById(buttonIds[selectedOrientation]).callOnClick();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mPreview.onPause();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

    }

    private File getStorageDir() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory("OralImaging"),
                "Set_" + timeStamp);

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory to store pictures");
                return null;
            }
        }
        return storageDir;
    }

    public Camera getCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch(Exception e) {
            Log.d(TAG, "Camera is not available.");
        }
        return c;
    }

    private class CaptureOrientationListener implements View.OnClickListener {

        private int[] buttonIds;

        public CaptureOrientationListener(int[] buttonIds) {
            this.buttonIds = buttonIds;
        }
        @Override
        public void onClick(View v) {
            v.setSelected(true);

            for (int i = 0; i < buttonIds.length; i++) {
                int id = buttonIds[i];
                if (id != v.getId()) {
                    findViewById(id).setSelected(false);
                } else {
                    selectedOrientation = i;
                }
            }
        }
    }
}

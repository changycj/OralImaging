package edu.mit.media.cameraculture.oralimaging.browse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import edu.mit.media.cameraculture.oralimaging.R;

/**
 * Created by judychang on 11/16/15.
 */
public class ViewPictureFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "ViewPictureFragment";

    private GestureDetector gestureDetector;

    private File[] picturesInSet;
    private String[] modeNames = new String[]{ "original.bmp", "rendered.bmp" };
    private String[] modeLabels = new String[] { "Original", "Rendered" };

    private int currPosition = 0;
    private int currMode = 0;

    private TextView labelView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        gestureDetector = new GestureDetector(this.getContext(), new GestureListener());

        View rootView = inflater.inflate(R.layout.fragment_view_picture, container, false);

        Bundle args = getArguments();
        String setPath = args.getString("setPath");
        currPosition = args.getInt("position");

        File setFolder = new File(setPath);

        picturesInSet = new File[] {
                new File(setFolder, "Top Left"),
                new File(setFolder, "Top Right"),
                new File(setFolder, "Bottom Left"),
                new File(setFolder, "Bottom Right")
        };

        imageView = (ImageView) rootView.findViewById(R.id.view_image);
        imageView.setOnTouchListener(this);
        labelView = (TextView) rootView.findViewById(R.id.view_label);

        updateImage();

        return rootView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight() {
        Log.d(TAG, "Swipe Right");
        if (currPosition - 1 >= 0) {
            currPosition = currPosition - 1;
        }
        updateImage();
    }

    public void onSwipeLeft() {
        Log.d(TAG, "Swipe Left");
        if (currPosition + 1 < picturesInSet.length) {
            currPosition = currPosition + 1;
        }
        updateImage();
    }

    public void onDblTap() {
        Log.d(TAG, "Double Tap");
        currMode = (currMode + 1) % modeNames.length;
        updateImage();
    }

    private void updateImage() {
        File img = new File(picturesInSet[currPosition], modeNames[currMode]);
        if (img.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            imageView.setImageBitmap(bitmap);

            String mode = modeLabels[currMode];
            String pictureName = picturesInSet[currPosition].getName();

            labelView.setText(String.format("%s: %s", pictureName, mode));
        }
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDblTap();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY)
                    && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                    && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }
}

package edu.mit.media.cameraculture.oralimaging;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import edu.mit.media.cameraculture.oralimaging.browse.BrowseFragment;
import edu.mit.media.cameraculture.oralimaging.capture.CaptureActivity;
import edu.mit.media.cameraculture.oralimaging.livefeed.LiveFeedActivity;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        File storageDir = Environment.getExternalStoragePublicDirectory("OralImaging");
        if (storageDir.exists()) {
            try {
                FileUtils.cleanDirectory(storageDir);
            } catch (IOException e) {
                Log.d(TAG, "Error cleaning: " + storageDir.getAbsolutePath());
            }
        } else {
            storageDir.mkdirs();
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BrowseFragment()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new BrowseFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_action_capture:
                Intent myIntent = new Intent(this, CaptureActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
                break;
            case R.id.main_action_livefeed:
                Intent myIntent2 = new Intent(this, LiveFeedActivity.class);
                myIntent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent2);
                break;
            case android.R.id.home :
                getSupportFragmentManager().popBackStack();
                break;
            default :
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar()
                .setDisplayHomeAsUpEnabled(
                        getSupportFragmentManager().getBackStackEntryCount() > 0);
    }
}
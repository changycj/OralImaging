package edu.mit.media.cameraculture.oralimaging.browse;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import edu.mit.media.cameraculture.oralimaging.R;

/**
 * Created by judychang on 11/15/15.
 */
public class BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";

    private ListView browseSets;
    private ArrayAdapter<String> setsList;

    private String rootDir = Environment.getExternalStoragePublicDirectory("OralImaging").toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        browseSets = (ListView) rootView.findViewById(R.id.browse_folders);

        setupFoldersList();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        refreshFoldersList();
    }

    private void refreshFoldersList() {
        setsList.clear();
        File dir = new File(rootDir);
        setsList.addAll(dir.list());
    }

    private void setupFoldersList() {

        final File dir = new File(rootDir);

        setsList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        browseSets.setAdapter(setsList);

        String[] folders = dir.list();

        if (folders == null || folders.length == 0) {
            setsList.add("No folders available.");
        } else {
            setsList.addAll(folders);

            browseSets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String set = setsList.getItem((int) id);

                    ViewSetFragment viewSetFragment = new ViewSetFragment();

                    Bundle bundle = new Bundle();
                    File setFolder = new File(dir, set);
                    bundle.putString("setPath", setFolder.getPath());
                    viewSetFragment.setArguments(bundle);

                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.replace(R.id.container, viewSetFragment);
                    trans.addToBackStack(null);
                    trans.commit();
                }
            });
        }
    }
}

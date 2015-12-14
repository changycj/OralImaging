package edu.mit.media.cameraculture.oralimaging.browse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;

import edu.mit.media.cameraculture.oralimaging.R;
import edu.mit.media.cameraculture.oralimaging.util.ImageGridAdapter;

/**
 * Created by judychang on 11/28/15.
 */
public class ViewSetFragment extends Fragment {

    private static final String TAG = "ViewSetFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_view_set, container, false);

        Bundle args = getArguments();
        String setPath = args.getString("setPath");
        Log.d(TAG, "setPath: " + setPath);

        final File setFolder = new File(setPath);

        GridView grid = (GridView) rootView.findViewById(R.id.view_set_grid);

        File[] imageFolders = new File[] {
                new File(setFolder, "Top Left"),
                new File(setFolder, "Top Right"),
                new File(setFolder, "Bottom Left"),
                new File(setFolder, "Bottom Right")
        };

        final ImageGridAdapter adapter = new ImageGridAdapter(getContext(), imageFolders);

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File picFolder = (File) adapter.getItem(position);

                ViewPictureFragment viewPictureFragment = new ViewPictureFragment();
                Bundle bundle = new Bundle();
                bundle.putString("setPath", setFolder.getPath());
                bundle.putInt("position", position);
//                bundle.putString("picturePath", picFolder.getPath());
                viewPictureFragment.setArguments(bundle);

                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.container, viewPictureFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return rootView;
    }

}

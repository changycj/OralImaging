package edu.mit.media.cameraculture.oralimaging.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import edu.mit.media.cameraculture.oralimaging.R;

/**
 * Created by judychang on 11/28/15.
 */
public class ImageGridAdapter extends BaseAdapter {

    private Context context;
    private File[] images;

    public ImageGridAdapter(Context context, File[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.grid_view_picture_item, null);

            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_text);
            textView.setText(images[position].getName());

            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_picture);

            File image = new File(images[position], "original.bmp");
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            imageView.setImageBitmap(bitmap);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

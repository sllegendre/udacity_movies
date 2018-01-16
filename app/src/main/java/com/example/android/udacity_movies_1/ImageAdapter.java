package com.example.android.udacity_movies_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SLLegendre on 12/6/2017.
 */

public class ImageAdapter extends ArrayAdapter<Movie> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private final Context mContext;
    public ImageAdapter(@NonNull Context context, int resource, ArrayList<Movie> movies) {
        super(context, 0, movies);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for this position
        Movie currentMovie = getItem(position);


        //Check if view is being reused, otherwise inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        //Manipulate the data
        Picasso.with(mContext).load(currentMovie.getPoster(true)).into((ImageView) convertView);

        // Return the list item view that is now showing the appropriate data
        return convertView;

    }

}

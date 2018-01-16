package com.example.android.udacity_movies_1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * A Loader to get the Movies from the database
 * Loaders are cooler than AsyncTask
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Movie> loadInBackground() { // Actual logic outsourced
        Log.v(LOG_TAG,"loadInBackground was called.");
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchMovieData(mUrl);
    }


}


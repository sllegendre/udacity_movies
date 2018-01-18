package com.example.android.udacity_movies_1;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Movie>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    /**
     * Query-URL that will return the most recent Movies
     */

    public static final String API_KEY = "";
    public static final String REQUEST_URL = "http://api.themoviedb.org/3/movie/";
    Context context = this;
    /**
     * Adapter for the list of Movies
     */
    private ImageAdapter mAdapter;
    private ProgressBar bar;
    TextView emptyView;

    private String mOrderParam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar) findViewById(R.id.loading_spinner);


        // Create a new adapter that takes an empty list of Movies as input
        mAdapter = new ImageAdapter(this, 0, new ArrayList<Movie>());


        // Find a reference to the {@link GridView} in the layout
        GridView MovieGridView = (GridView) findViewById(R.id.gridview);
        MovieGridView.setEmptyView(findViewById(R.id.empty));


        //Check for Network connectivity
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        // Set the adapter on the {@link GridView}
        // so the list can be populated in the user interface
        MovieGridView.setAdapter(mAdapter);

        // Set a click listener to take me to the DetailActivity
        MovieGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        Movie currentMovie = mAdapter.getItem(position);
                        Intent i = new Intent(getApplicationContext(),DetailActivity.class);
                        // Set Extras
                        i.putExtra("title", currentMovie.getTitle());
                        i.putExtra("releaseDate", currentMovie.getReleaseDate());
                        i.putExtra("overview", currentMovie.getOverview());
                        i.putExtra("rating", currentMovie.getRating());
                        i.putExtra("poster", currentMovie.getPoster(true));
                        i.putExtra("onlineId", currentMovie.getOnlineId());
                        startActivity(i);
                    }
                });


        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Log.v(LOG_TAG, "no internet connection");
            bar.setVisibility(View.GONE);
            emptyView = (TextView) findViewById(R.id.empty);
            emptyView.setText(R.string.no_connectivity);

        } else {
            Log.v(LOG_TAG, "we have internet connection");
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //find out how to sort the movies
        //find out how to sort the movies
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        if(orderBy!=mOrderParam){
            getLoaderManager().restartLoader(0, null, this);
        }


    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {

        //find out how to sort the movies
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        mOrderParam = orderBy; //I am setting this here to be able to chek it onResume() - ugly workaround... please help Udacity!

        String settingsString = "popular";
        if(!orderBy.equals("popularity")){
            settingsString = "top_rated";
        }

        Uri baseUri = Uri.parse(REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);
        Log.v(LOG_TAG, uriBuilder.toString());


        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.v(LOG_TAG, "onLoaderFinished was called.");


        bar.setVisibility(View.GONE);

        // Clear the adapter of previous Movie data
        mAdapter.clear();

        // If there is a valid list of {@link Movie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

        emptyView = (TextView) findViewById(R.id.empty);
        emptyView.setText(R.string.no_Movies);

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


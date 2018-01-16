package com.example.android.udacity_movies_1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.udacity_movies_1.data.FavoritesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by SLLegendre on 06.01.2018.
 */

public class DetailActivity extends AppCompatActivity {
    /**
     * For Log
     */
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    //members
    String title;
    String releaseDate;
    String overview;
    String rating;
    String poster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Context mContext = getApplicationContext();

        // Find views
        ImageView posterView = (ImageView) findViewById(R.id.posterView);
        TextView titleView = (TextView) findViewById(R.id.titleView);
        TextView ratingView = (TextView) findViewById(R.id.ratingView);
        TextView releaseDateView = (TextView) findViewById(R.id.releaseView);
        TextView overviewView = (TextView) findViewById(R.id.overviewView);

        //Get Extras
        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        releaseDate = intent.getStringExtra("releaseDate");
        overview = intent.getStringExtra("overview");
        rating = intent.getStringExtra("rating");
        poster = intent.getStringExtra("poster");

        //Set texts
        titleView.setText(title);
        releaseDateView.setText(releaseDate);
        overviewView.setText(overview);
        ratingView.setText(rating);

        //Set image
        Picasso.with(mContext).load(poster).into(posterView);


    }

    /**
     * Adds the film to the database
     * @param view
     */
    public void addToFavorites(View view) {

        // 1. Set up data to insert
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE,title);

        // 2. Make use of content resolver
        Uri uri = getContentResolver().insert(FavoritesContract.FavoriteItem.CONTENT_URI,contentValues);

        if(uri !=null){
            Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_LONG).show();
        }
    }
}

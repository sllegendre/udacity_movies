package com.example.android.udacity_movies_1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    int onlineId;
    Button favorite;
    Button unfavorite;

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
        favorite = (Button) findViewById(R.id.favorite_button);
        unfavorite = (Button) findViewById(R.id.unfavorite_button);
        unfavorite.setVisibility(View.GONE);

        //Get Extras
        Intent intent = getIntent();

        title = intent.getStringExtra("title");
        releaseDate = intent.getStringExtra("releaseDate");
        overview = intent.getStringExtra("overview");
        rating = intent.getStringExtra("rating");
        poster = intent.getStringExtra("poster");
        onlineId = intent.getIntExtra("onlineId",0);


        //Set texts
        titleView.setText(title);
        releaseDateView.setText(releaseDate);
        overviewView.setText(overview);
        ratingView.setText(rating);

        //Set image
        Picasso.with(mContext).load(poster).into(posterView);

        // Find out if the movie has previously been added to favorites
        checkIfFavorite(onlineId);




    }


    public boolean checkIfFavorite(int onlineId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE,title);
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID, onlineId);

       // Uri uri = getContentResolver().query(FavoritesContract.FavoriteItem.CONTENT_URI,contentValues);

        return false;
    }

    /**
     * Adds the film to the database
     * @param view
     */
    public void addToFavorites(View view) {

        // 1. Set up data to insert
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE,title);
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID, onlineId);

        Log.e(LOG_TAG,"content values are " + contentValues.toString());

        // 2. Make use of content resolver
        Uri uri = getContentResolver().insert(FavoritesContract.FavoriteItem.CONTENT_URI,contentValues);

        if(uri !=null){
            Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_LONG).show();
            favorite.setVisibility(View.GONE);
            unfavorite.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Removes the film from the database
     * @param view
     */
    public void removeFromFavorites(View view) {

        // 1. Set up data to insert
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE,title);
        contentValues.put(FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID, onlineId);

        Log.e(LOG_TAG,"content values are " + contentValues.toString());

        // 2. Make use of content resolver
        Uri uri = getContentResolver().insert(FavoritesContract.FavoriteItem.CONTENT_URI,contentValues);

        if(uri !=null){
            Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_LONG).show();
            unfavorite.setVisibility(View.GONE);
            favorite.setVisibility(View.VISIBLE);
        }

    }

}

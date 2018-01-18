package com.example.android.udacity_movies_1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Database helper for udacity movies 2 app. Manages database creation and version management.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavoritesDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "favorites.db";

    /**
     * Database version. If you change the database schema, increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link FavoritesDbHelper}.
     *
     * @param context of the app
     */
    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the favorites table
        String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteItem.TABLE_NAME + " ("
                + FavoritesContract.FavoriteItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE + " TEXT NOT NULL,"
                + FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE "
                + ")" + ";";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This part I really do not understand...
    }

    // Utility functions, I may need should I go for the extra credit...

    // convert from bitmap to byte array
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap convertByteArrayToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}

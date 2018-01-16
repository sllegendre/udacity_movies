package com.example.android.udacity_movies_1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SLLegendre on 1/13/2018.
 */

public class FavoritesContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FavoritesContract() {
    }

    /* Foundations for URI */
    public static final String CONTENT_AUTHORITY = "com.example.android.udacity_movies_1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    /**
     * Inner class that defines constant values for the database table.
     * Each entry in the table represents a single movie that has been 'favorited'
     */
    public static final class FavoriteItem implements BaseColumns {

        /* The content URI to access the data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);

        /* The MIME type of the {@link #CONTENT_URI} for a list of favorite movies. */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single movie.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /**
         * Table constants for table: favorites
         * TODO?: add movie poster, synopsis, user rating, and release date, and display them even when offline
         **/

        public final static String TABLE_NAME = "favorites";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FAVORITE_TITLE = "title";
        //public final static String COLUMN_FAVORITE_ID = "id";


    }

}

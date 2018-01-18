package com.example.android.udacity_movies_1.data;

/**
 * Created by SLLegendre on 1/13/2018. Adapted from a project I did for the ABND
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

//import static com.example.android.udacity_movies_1.data.FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID;
import static com.example.android.udacity_movies_1.data.FavoritesContract.FavoriteItem.COLUMN_FAVORITE_ID;
import static com.example.android.udacity_movies_1.data.FavoritesContract.FavoriteItem.COLUMN_FAVORITE_TITLE;
import static com.example.android.udacity_movies_1.data.FavoritesContract.FavoriteItem.TABLE_NAME;

/**
 *
 */

public class FavoritesProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = FavoritesProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the entire table
     */
    private static final int FAVORITES = 100;

    /**
     * URI matcher code for the content URI for a single item in the inventory table
     */
    private static final int FAVORITES_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.inventoryapp/inventory" will map to the
        // integer code {@link #FAVORITES}. This URI is used to provide access to MULTIPLE rows
        // of the table.
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);

        // The content URI of the form "content://com.example.android.inventoryapp/inventory/#" will map to the
        // integer code {@link #INVENTROY_ID}. This URI is used to provide access to ONE single row
        // of the table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.#TODO
        // For example, "content://com.example.android.inventoryapp/inventory/3" matches, but
        // "content://com.example.android.inventoryapp/inventory" (without a number at the end) doesn't match.
        sUriMatcher.addURI(FavoritesContract.CONTENT_AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", FAVORITES_ID);
    }

    /**
     * Database helper object
     */
    private com.example.android.udacity_movies_1.data.FavoritesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new com.example.android.udacity_movies_1.data.FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // For the FAVORITES code, query the table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the table.
                cursor = database.query(FavoritesContract.FavoriteItem.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVORITES_ID:
                // For the FAVORITES_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.inventoryapp/inventory/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FavoritesContract.FavoriteItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the inventory table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FavoritesContract.FavoriteItem.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return FavoritesContract.FavoriteItem.CONTENT_LIST_TYPE;
            case FAVORITES_ID:
                return FavoritesContract.FavoriteItem.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                Log.e(LOG_TAG, "the insert method is being executed for "+contentValues.toString());
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(FavoritesContract.FavoriteItem.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_ID:
                // Delete a single row given by the ID in the URI
                selection = FavoritesContract.FavoriteItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FavoritesContract.FavoriteItem.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Insert an item into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertItem(Uri uri, ContentValues values) {
        // Check that the title is not null

        Log.e(LOG_TAG, "the insertItem method is being executed for "+values.toString());
        String name = values.getAsString(COLUMN_FAVORITE_TITLE);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }

        // Check that the id is valid
        Integer favoriteId = values.getAsInteger(COLUMN_FAVORITE_ID);
        if (favoriteId == null || favoriteId < 0) {
            throw new IllegalArgumentException("Item requires valid id");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new item with the given values
        long id = database.insert(FavoritesContract.FavoriteItem.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }else{
            Log.e(LOG_TAG, "Insertion successful for "+values.toString()+"with id: "+String.valueOf(id));
        }

        // Notify all listeners that the data has changed for the favorites content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        Log.e(LOG_TAG,ContentUris.withAppendedId(uri, id).toString());
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        Log.v(LOG_TAG, "The update method was called with URI:" + uri.toString() + " and values: " + contentValues.toString());
        switch (match) {
            case FAVORITES:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case FAVORITES_ID:
                // For the FAVORITES_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = FavoritesContract.FavoriteItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update items in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more items).
     * Return the number of rows that were successfully updated.
     */
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link FavoriteItem#COLUMN_FAVORITE_TITLE} key is present,
        // check that the name value is not null.
        if (values.containsKey(COLUMN_FAVORITE_TITLE)) {
            String name = values.getAsString(COLUMN_FAVORITE_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a title");
            }
        }



        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        Log.v(LOG_TAG, "Trying to update: table: " + TABLE_NAME.toString() + " values: " + values.toString() + " selection: " + selection.toString() + " selectionArgs: " + selectionArgs.toString());
        int rowsUpdated = database.update(FavoritesContract.FavoriteItem.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        Log.v(LOG_TAG, rowsUpdated + " of rows updated");
        return rowsUpdated;
    }


}


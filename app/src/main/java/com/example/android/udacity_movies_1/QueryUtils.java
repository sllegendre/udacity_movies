package com.example.android.udacity_movies_1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for requesting and receiving movie data
 */

public class QueryUtils {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * We should never want an instance of this, thus a private constructor
     */
    private QueryUtils() {
    }

    /**
     * Query the themoviedb dataset and return a list of {@link Movie} objects.
     */
    public static List<Movie> fetchMovieData(String requestUrl) {

        Log.v(LOG_TAG,"fetching movie data...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        List<Movie> Movies = extractMovies(jsonResponse);

        // Return the list of {@link Movie}s
        return Movies;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        int responseCodeInt = -1000;
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        } // no need to even try with a null URL
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            responseCodeInt = urlConnection.getResponseCode();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.v(LOG_TAG, String.valueOf(responseCodeInt) + "is -1000 if it has never been initialized");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Movie> extractMovies(String jsonResponse) {

        // Create an empty ArrayList that we can start adding Movies to
        ArrayList<Movie> Movies = new ArrayList<>();

        // Try to parse the result If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            //convert to jsonObject
            JSONObject jsonObject = new JSONObject(jsonResponse);

            //find films
            JSONArray jsonResults = jsonObject.getJSONArray("results");

            //loop through dem f*ckers and append them to List
            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject film = jsonResults.getJSONObject(i);

                // Extract “vote_average" for rating
                double rating = film.getDouble("vote_average");
                // Extract “original_title" for the films title
                String title = film.getString("original_title");
                //continue for other members of Movie...
                String summary = film.getString("overview");
                // Extract the url param to get the poster
                String poster = film.getString("poster_path");
                String releaseDate = film.getString("release_date");
                // Create Movie java object
                Movie newMovie = new Movie(title, poster, summary, rating, releaseDate);
                // Add Movie to list of Movies
                Movies.add(newMovie);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the Movie JSON results", e);
        }

        // Return the list of Movies
        return Movies;
    }

}
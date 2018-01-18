package com.example.android.udacity_movies_1;

/**
 * Everything we know about a movie to display on screen.
 * Pretty boilerplate
 */

public class Movie {


    private String mTitle;
    private String mPoster;
    private String mSummary;
    private Double mRating;
    private String mReleaseDate;
    private int mOnlineId;

    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    public Movie(String mTitle, String mPoster, String mOverview, Double mRating, String mReleaseDate, int mOnlineId) {
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mSummary = mOverview;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
        this.mOnlineId = mOnlineId;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     *
     * @param addURL boolean whether you want the poster raw or contained in the a URL
     * @return the poster ID if addURL false, otherwise the url to request the image
     */
    public String getPoster(boolean addURL) {
        String ret = mPoster;
        if (addURL) {
            ret = "http://image.tmdb.org/t/p/" + "w185/" + mPoster;
        }
        return ret;
    }

    public String getOverview() {
        return mSummary;
    }

    public String getRating() {
        String stringRating = mRating.toString();
        return stringRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getOnlineId(){
        return mOnlineId;
    }
}

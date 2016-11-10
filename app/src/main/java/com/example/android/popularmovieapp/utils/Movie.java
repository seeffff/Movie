package com.example.android.popularmovieapp.utils;

public class Movie {

    private long mId;
    private String mMovieId;
    private String mTitle;
    private String mDate;
    private String mSynopsis;
    private String mVote;
    private String mPoster;
    private String mBackdrop;

    public Movie(String movieId, String title, String date, String synopsis, String vote, String poster, String backdrop) {
        mMovieId = movieId;
        mTitle = title;
        mDate = date;
        mSynopsis = synopsis;
        mVote = vote;
        mPoster = poster;
        mBackdrop = backdrop;
    }

    public Movie(String movieId, String title, String date, String synopsis, String vote, String poster, String backdrop, long id) {
        mMovieId = movieId;
        mTitle = title;
        mDate = date;
        mSynopsis = synopsis;
        mVote = vote;
        mPoster = poster;
        mBackdrop = backdrop;
        mId = id;
    }


    public String getMovieId() {
        return mMovieId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getVote() {
        return mVote;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public long getId() {
        return mId;
    }
}

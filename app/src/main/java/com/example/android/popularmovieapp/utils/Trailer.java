package com.example.android.popularmovieapp.utils;

public class Trailer {

    private String mTrailerKey;
    private String mTrailerType;

    public Trailer(String key, String type) {
        mTrailerKey = key;
        mTrailerType = type;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public String getTrailerType() {
        return mTrailerType;
    }

}

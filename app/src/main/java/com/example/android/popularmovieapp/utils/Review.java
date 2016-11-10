package com.example.android.popularmovieapp.utils;

public class Review {

    private String mAuthor;
    private String mContent;

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

}

package com.example.android.popularmovieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.popularmovieapp.frags.MovieDetailFragment;
import com.example.android.popularmovieapp.frags.MovieDisplayFragment;

public class MainActivity extends AppCompatActivity implements MovieDisplayFragment.Callback {

    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Bundle args) {

        String movieId = args.getString("MovieId");
        String movieTitle = args.getString("Title");
        String movieDate = args.getString("Date");
        String movieSynopsis = args.getString("Synopsis");
        String movieVote = args.getString("Vote");
        String moviePoster = args.getString("Poster");
        String movieBackdrop = args.getString("Backdrop");

        if (mTwoPane) {

            Bundle detailArgs = new Bundle();
            detailArgs.putString("MovieId", movieId);
            detailArgs.putString("Title", movieTitle);
            detailArgs.putString("Date", movieDate);
            detailArgs.putString("Synopsis", movieSynopsis);
            detailArgs.putString("Vote", movieVote);
            detailArgs.putString("Poster", moviePoster);
            detailArgs.putString("Backdrop", movieBackdrop);

            MovieDetailFragment df = new MovieDetailFragment();
            df.setArguments(detailArgs);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, df, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra("MovieId", movieId);
            i.putExtra("Title", movieTitle);
            i.putExtra("Date", movieDate);
            i.putExtra("Synopsis", movieSynopsis);
            i.putExtra("Vote", movieVote);
            i.putExtra("Poster", moviePoster);
            i.putExtra("Backdrop", movieBackdrop);
            startActivity(i);
        }
    }
}

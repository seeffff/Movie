package com.example.android.popularmovieapp.frags;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovieapp.R;
import com.example.android.popularmovieapp.utils.Movie;
import com.example.android.popularmovieapp.utils.MovieDbAdapter;
import com.example.android.popularmovieapp.utils.Review;
import com.example.android.popularmovieapp.utils.ReviewAdapter;
import com.example.android.popularmovieapp.utils.Trailer;
import com.example.android.popularmovieapp.utils.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    ListView reviewListView, trailerListView;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent i = getActivity().getIntent();
        Bundle detailArgs = getArguments();

        if (i.getStringExtra("Title") == null && detailArgs == null) {
            return inflater.inflate(R.layout.fragment_detail_null, container, false);
        } else if (detailArgs != null) {

            setBackdrop set = new setBackdrop();
            set.execute(detailArgs.getString("Backdrop"));

            String title = detailArgs.getString("Title");
            String date = detailArgs.getString("Date");
            String synopsis = detailArgs.getString("Synopsis");
            String posterPath = detailArgs.getString("Poster");
            String vote = detailArgs.getString("Vote");

            ((TextView) rootView.findViewById(R.id.movie_detail_title))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.movie_detail_date))
                    .setText(date);
            ((TextView) rootView.findViewById(R.id.movie_detail_synopsis))
                    .setText(synopsis);
            ((TextView) rootView.findViewById(R.id.movie_detail_vote))
                    .setText(vote);

            ImageView poster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
            String movieUrl = "https://image.tmdb.org/t/p/w300" + posterPath;
            Picasso.with(getContext()).load(movieUrl).resize(400, 600).into(poster);


            final Button favoriteButton = (Button) rootView.findViewById(R.id.add_favorite);

            final MovieDbAdapter dbAdapter = new MovieDbAdapter(getActivity());
            dbAdapter.open();
            boolean doesExist = dbAdapter.movieExists(i.getStringExtra("MovieId"));
            if (!doesExist) {
                favoriteButton.setText(R.string.add_to_favorites);
            } else {
                favoriteButton.setText(R.string.in_favorites);
            }
            dbAdapter.close();

            updateMovieReviewsTrailers(detailArgs.getString("MovieId"));
            reviewListView = (ListView) rootView.findViewById(R.id.review_list);
            reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
            reviewListView.setAdapter(reviewAdapter);
            trailerListView = (ListView) rootView.findViewById(R.id.trailer_list);
            trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
            trailerListView.setAdapter(trailerAdapter);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MovieDbAdapter dbAdapter = new MovieDbAdapter(getActivity());

                    Bundle detailArgs = getArguments();

                    dbAdapter.open();
                    boolean doesExist = dbAdapter.movieExists(detailArgs.getString("MovieId"));

                    if (!doesExist) {
                        Movie movie = new Movie(detailArgs.getString("MovieId"), detailArgs.getString("Title"),
                                detailArgs.getString("Date"), detailArgs.getString("Synopsis"), detailArgs.getString("Vote"),
                                detailArgs.getString("Poster"), detailArgs.getString("Backdrop"));
                        dbAdapter.createTableMovie(movie);
                        favoriteButton.setText(R.string.in_favorites);
                    } else {
                        dbAdapter.deleteMovie(detailArgs.getString("MovieId"));
                        favoriteButton.setText(R.string.add_to_favorites);
                    }
                    dbAdapter.close();
                }
            });

        } else {
            String title = i.getStringExtra("Title");
            String date = i.getStringExtra("Date");
            String synopsis = i.getStringExtra("Synopsis");
            String posterPath = i.getStringExtra("Poster");
            String vote = i.getStringExtra("Vote");

            ((TextView) rootView.findViewById(R.id.movie_detail_title))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.movie_detail_date))
                    .setText(date);
            ((TextView) rootView.findViewById(R.id.movie_detail_synopsis))
                    .setText(synopsis);
            ((TextView) rootView.findViewById(R.id.movie_detail_vote))
                    .setText(vote);

            ImageView poster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
            String movieUrl = "https://image.tmdb.org/t/p/w300" + posterPath;
            Picasso.with(getContext()).load(movieUrl).resize(400, 600).into(poster);


            final Button favoriteButton = (Button) rootView.findViewById(R.id.add_favorite);

            final MovieDbAdapter dbAdapter = new MovieDbAdapter(getActivity());
            dbAdapter.open();
            boolean doesExist = dbAdapter.movieExists(i.getStringExtra("MovieId"));
            if (!doesExist) {
                favoriteButton.setText(R.string.add_to_favorites);
            } else {
                favoriteButton.setText(R.string.in_favorites);
            }
            dbAdapter.close();

            updateMovieReviewsTrailers(i.getStringExtra("MovieId"));
            reviewListView = (ListView) rootView.findViewById(R.id.review_list);
            reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
            reviewListView.setAdapter(reviewAdapter);
            trailerListView = (ListView) rootView.findViewById(R.id.trailer_list);
            trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
            trailerListView.setAdapter(trailerAdapter);

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MovieDbAdapter dbAdapter = new MovieDbAdapter(getActivity());

                    Intent i = getActivity().getIntent();

                    dbAdapter.open();
                    boolean doesExist = dbAdapter.movieExists(i.getStringExtra("MovieId"));

                    if (!doesExist) {
                        Movie movie = new Movie(i.getStringExtra("MovieId"), i.getStringExtra("Title"),
                                i.getStringExtra("Date"), i.getStringExtra("Synopsis"), i.getStringExtra("Vote"),
                                i.getStringExtra("Poster"), i.getStringExtra("Backdrop"));
                        dbAdapter.createTableMovie(movie);
                        favoriteButton.setText(R.string.in_favorites);
                    } else {
                        dbAdapter.deleteMovie(i.getStringExtra("MovieId"));
                        favoriteButton.setText(R.string.add_to_favorites);
                    }
                    dbAdapter.close();
                }
            });
        }
        return rootView;
    }

    private void updateMovieReviewsTrailers(String str) {
        getReviews reviewData = new getReviews();
        reviewData.execute(str);
        getTrailers trailerData = new getTrailers();
        trailerData.execute(str);
    }

    class setBackdrop extends AsyncTask<String, Void, Bitmap> {

        public Bitmap doInBackground(String... urls) {
            Bitmap bm = null;
            try {
                URL aURL = new URL("https://image.tmdb.org/t/p/w300" + urls[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("img", "Error getting bitmap", e);
            }
            return bm;
        }

        protected void onPostExecute(Bitmap backdrop) {
            ScrollView layout = (ScrollView) getActivity().findViewById(R.id.detail_frag);

            if(isAdded()) {
                try {
                    Bitmap transBackdrop = makeTransparent(backdrop, 60);
                    BitmapDrawable adjustedBackdrop = new BitmapDrawable(getResources(), transBackdrop);
                    layout.setBackgroundDrawable(adjustedBackdrop);
                } catch (NullPointerException e) {

                }
            }
        }

        public Bitmap makeTransparent(Bitmap src, int value) {
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(transBitmap);
            canvas.drawARGB(0, 0, 0, 0);

            final Paint paint = new Paint();
            paint.setAlpha(value);
            canvas.drawBitmap(src, 0, 0, paint);

            return transBitmap;
        }
    }

    class getReviews extends AsyncTask<String, Void, List<Review>> {

        private List<Review> getReviews(String movieReviewJson) throws JSONException {
            final String REV_RESULTS = "results";
            final String REV_AUTHOR = "author";
            final String REV_CONTENT = "content";

            if (movieReviewJson != null) {
                JSONObject movieJson = new JSONObject(movieReviewJson);

                JSONArray reviewArray = movieJson.getJSONArray(REV_RESULTS);

                for (int i = 0; i < reviewArray.length(); i++) {

                    JSONObject review = reviewArray.getJSONObject(i);

                    reviewList.add(new Review(review.getString(REV_AUTHOR), review.getString(REV_CONTENT)));
                }
            }
            return reviewList;
        }

        protected List<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieReviewStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "/reviews?api_key=358f3b44734f7e6404f2d01a62d3c176";
                Uri builtUri = Uri.parse(BASE_URL + params[0] + API_KEY).buildUpon().build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieReviewStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    movieReviewStr = buffer.toString();
                }
                movieReviewStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Fragment", "Error", e);
                movieReviewStr = null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviews(movieReviewStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(List<Review> reviews) {
            reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
            reviewListView.setAdapter(reviewAdapter);
        }
    }

    class getTrailers extends AsyncTask<String, Void, List<Trailer>> {

        private List<Trailer> getTrailers(String movieTrailJson) throws JSONException {
            final String TRAIL_RESULTS = "results";
            final String TRAIL_KEY = "key";
            final String TRAIL_TYPE = "type";

            if (movieTrailJson != null) {
                JSONObject movieJson = new JSONObject(movieTrailJson);

                JSONArray trailArray = movieJson.getJSONArray(TRAIL_RESULTS);

                for (int i = 0; i < trailArray.length(); i++) {

                    JSONObject review = trailArray.getJSONObject(i);

                    trailerList.add(new Trailer(review.getString(TRAIL_KEY), review.getString(TRAIL_TYPE)));
                }
            }
            return trailerList;
        }

        protected List<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieReviewStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "/videos?api_key=358f3b44734f7e6404f2d01a62d3c176";
                Uri builtUri = Uri.parse(BASE_URL + params[0] + API_KEY).buildUpon().build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieReviewStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    movieReviewStr = buffer.toString();
                }
                movieReviewStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Fragment", "Error", e);
                movieReviewStr = null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailers(movieReviewStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(List<Trailer> trailers) {
            trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
            trailerListView.setAdapter(trailerAdapter);
        }
    }
}

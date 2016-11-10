package com.example.android.popularmovieapp.frags;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.popularmovieapp.R;
import com.example.android.popularmovieapp.utils.Movie;
import com.example.android.popularmovieapp.utils.MovieAdapter;
import com.example.android.popularmovieapp.utils.MovieDbAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class MovieDisplayFragment extends Fragment {

    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    private MovieAdapter movieAdapter;
    GridView gridView;
    TextView noInternetText;

    public interface Callback {
        void onItemSelected(Bundle args);
    }

    public MovieDisplayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();

        int id = item.getItemId();
        if (id == R.id.sort_popularity_desc) {
            movieAdapter.clear();
            updateMovieData("popular");
            editor.putString("Sort", "popular");
            editor.commit();
            return true;
        } else if (id == R.id.sort_vote_desc) {
            movieAdapter.clear();
            updateMovieData("top_rated");
            editor.putString("Sort", "top_rated");
            editor.commit();
            return true;
        } else if (id == R.id.sort_now_playing) {
            movieAdapter.clear();
            updateMovieData("now_playing");
            editor.putString("Sort", "now_playing");
            editor.commit();
            return true;
        } else if (id == R.id.sort_favorites) {
            editor.putString("Sort", "favorites");
            editor.commit();
            movieAdapter.clear();
            final MovieDbAdapter dbAdapter = new MovieDbAdapter(getActivity());
            dbAdapter.open();
            movieList = dbAdapter.getAllMovies();
            dbAdapter.close();
            movieAdapter = new MovieAdapter(getActivity(), movieList);
            gridView.setAdapter(movieAdapter);
            noInternetText.setVisibility(View.GONE);
            if (movieList.size() == 0) {
                noInternetText.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String sort;

        if (movieList.size() == 0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            sort = prefs.getString("Sort", "popular");
            updateMovieData(sort);
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            noInternetText = (TextView) rootView.findViewById(R.id.no_internet);
            gridView = (GridView) rootView.findViewById(R.id.listview_data);

            movieAdapter = new MovieAdapter(getActivity(), movieList);
            gridView.setAdapter(movieAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Movie movie = movieAdapter.getItem(position);

                    Bundle args = new Bundle();
                    args.putString("MovieId", movie.getMovieId());
                    args.putString("Title", movie.getTitle());
                    args.putString("Date", movie.getDate());
                    args.putString("Synopsis", movie.getSynopsis());
                    args.putString("Vote", movie.getVote());
                    args.putString("Poster", movie.getPoster());
                    args.putString("Backdrop", movie.getBackdrop());
                    args.putLong("Id", movie.getId());

                    ((Callback) getActivity()).onItemSelected(args);
                }
            });
            return rootView;

    }

    private void updateMovieData(String str) {
        getMovieData movieData = new getMovieData();
        movieData.execute(str);
    }

    public class getMovieData extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_CAT = getMovieData.class.getSimpleName();
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading movies...");
            dialog.show();
        }

        private List<Movie> getMovieData(String movieJsonStr) throws JSONException {

            final String MOV_ITEMS = "results";
            final String MOV_ID = "id";
            final String MOV_TITLE = "original_title";
            final String MOV_DATE = "release_date";
            final String MOV_SYNOPSIS = "overview";
            final String MOV_VOTE = "vote_average";
            final String MOV_POSTER = "poster_path";
            final String MOV_BACKDROP = "backdrop_path";

            if (movieJsonStr != null) {
                JSONObject movieJson = new JSONObject(movieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MOV_ITEMS);

                for (int i = 0; i < movieArray.length(); i++) {

                    JSONObject movie = movieArray.getJSONObject(i);

                    movieList.add(new Movie(movie.getString(MOV_ID), movie.getString(MOV_TITLE), movie.getString(MOV_DATE),
                            movie.getString(MOV_SYNOPSIS), movie.getString(MOV_VOTE),
                            movie.getString(MOV_POSTER),
                            movie.getString(MOV_BACKDROP)));
                }
            }
            return movieList;
        }

        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY = "?api_key=358f3b44734f7e6404f2d01a62d3c176";
                Uri builtUri = Uri.parse(BASE_URL + params[0] + API_KEY).buildUpon().build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    movieJsonStr = buffer.toString();
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_CAT, "Movie String: " + movieJsonStr);
            } catch (IOException e) {
                Log.e("Fragment", "Error", e);
                movieJsonStr = null;

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
                return getMovieData(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_CAT, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            movieAdapter = new MovieAdapter(getActivity(), movieList);

            gridView.setAdapter(movieAdapter);
            noInternetText.setVisibility(View.GONE);
            dialog.dismiss();
            if (movieList.size() == 0) {
                noInternetText.setVisibility(View.VISIBLE);
            }
        }
    }
}

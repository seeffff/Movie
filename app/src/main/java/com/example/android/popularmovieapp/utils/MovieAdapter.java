package com.example.android.popularmovieapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.popularmovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> implements ListAdapter {

    public static class ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieRating;
        TextView movieId;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Movie movie = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movie, parent, false);

            viewHolder.moviePoster = (ImageView) convertView.findViewById(R.id.poster_path);
            viewHolder.movieTitle = (TextView) convertView.findViewById(R.id.movie_title);
            viewHolder.movieRating = (TextView) convertView.findViewById(R.id.movie_rating);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (movie.getPoster() != null) {
            String movieUrl = "https://image.tmdb.org/t/p/w300" + movie.getPoster();
            Picasso.with(getContext()).load(movieUrl).resize(400, 600).into(viewHolder.moviePoster);
        }
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.movieRating.setText(movie.getVote());

        return convertView;
    }

}



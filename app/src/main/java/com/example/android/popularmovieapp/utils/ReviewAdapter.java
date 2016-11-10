package com.example.android.popularmovieapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.popularmovieapp.R;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> implements ListAdapter {

    public static class ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;
    }

    public ReviewAdapter(Context context, ArrayList<Review> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Review review = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_review, parent, false);

            viewHolder.reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
            viewHolder.reviewContent = (TextView) convertView.findViewById(R.id.review_content);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.reviewAuthor.setText(review.getAuthor());
        viewHolder.reviewContent.setText(review.getContent());

        return convertView;
    }
}

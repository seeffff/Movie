package com.example.android.popularmovieapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.popularmovieapp.R;

import java.util.ArrayList;

public class TrailerAdapter extends ArrayAdapter<Trailer> implements ListAdapter {

    public static class ViewHolder {
        TextView trailerText;
        Button trailerButton;
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Trailer trailer = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_trailer, parent, false);

            viewHolder.trailerText = (TextView) convertView.findViewById(R.id.trailer_text);
            viewHolder.trailerButton = (Button) convertView.findViewById(R.id.play_button);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.trailerText.setText(trailer.getTrailerType());

        viewHolder.trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtube = "http://www.youtube.com/watch?v=" + trailer.getTrailerKey();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}

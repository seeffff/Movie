package com.example.android.popularmovieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.os.Bundle;
import android.widget.ScrollView;

import com.example.android.popularmovieapp.frags.MovieDetailFragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DetailActivity extends AppCompatActivity {

    setBackdrop set = new setBackdrop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }

        Intent i = getIntent();

        if (i.getStringExtra("Backdrop") != null) {
            String backdrop = i.getStringExtra("Backdrop");
            set.execute(backdrop);
        }
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
            ScrollView layout = (ScrollView) findViewById(R.id.detail_frag);

            try {
                Bitmap transBackdrop = makeTransparent(backdrop, 60);
                BitmapDrawable adjustedBackdrop = new BitmapDrawable(getResources(), transBackdrop);
                layout.setBackgroundDrawable(adjustedBackdrop);
            } catch (NullPointerException e) {
                Log.e("Error", ": Error");
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

}

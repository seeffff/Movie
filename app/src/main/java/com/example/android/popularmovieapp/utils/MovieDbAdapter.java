package com.example.android.popularmovieapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MovieDbAdapter {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public static final String MOVIE_TABLE = "movie";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ID = "movieid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SYNOPSIS = "synopsis";
    public static final String COLUMN_VOTE = "vote";
    public static final String COLUMN_BACKDROP = "backdrop";
    public static final String COLUMN_POSTER = "poster";

    //Array list of columns in the table
    public String[] allColumns = {COLUMN_ID, COLUMN_MOVIE_ID, COLUMN_TITLE, COLUMN_DATE,
            COLUMN_SYNOPSIS, COLUMN_VOTE, COLUMN_BACKDROP, COLUMN_POSTER};

    //String used to create the table
    public static final String CREATE_TABLE_MOVIE = " create table " + MOVIE_TABLE + " ( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MOVIE_ID + " text not null, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_SYNOPSIS + " text not null, "
            + COLUMN_VOTE + " text not null, "
            + COLUMN_BACKDROP + " text not null, "
            + COLUMN_POSTER + " text not null " + " ); ";
    private SQLiteDatabase sqlDB;
    private MovieDbHelper movieDbHelper;
    private Context context;

    public MovieDbAdapter(Context ctx) {
        context = ctx;
    }

    //Opens the database adapter
    public MovieDbAdapter open() throws android.database.SQLException {
        movieDbHelper = new MovieDbHelper(context);
        sqlDB = movieDbHelper.getWritableDatabase();
        return this;
    }

    //Closes the database adapter
    public void close() {
        movieDbHelper.close();
    }

    //Method used to create an item
    public void createTableMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getMovieId());
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_DATE, movie.getDate());
        values.put(COLUMN_SYNOPSIS, movie.getSynopsis());
        values.put(COLUMN_VOTE, movie.getVote());
        values.put(COLUMN_POSTER, movie.getPoster());
        values.put(COLUMN_BACKDROP, movie.getBackdrop());

        sqlDB.insert(MOVIE_TABLE, null, values);
    }

    public boolean movieExists(String movieId) {
        String Query = "Select * from " + MOVIE_TABLE + " where " + COLUMN_MOVIE_ID + " = " + movieId;
        Cursor cursor = sqlDB.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //Method used to delete an item
    public long deleteMovie(String idToDelete) {
        return sqlDB.delete(MOVIE_TABLE, COLUMN_MOVIE_ID + " = " + idToDelete, null);
    }

    //Will return all items in the table as an array list
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        Cursor cursor = sqlDB.query(MOVIE_TABLE, allColumns, null, null, null, null, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            String movieId = (cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID)));
            String title = (cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            String date = (cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            String synopsis = (cursor.getString(cursor.getColumnIndex(COLUMN_SYNOPSIS)));
            String vote = (cursor.getString(cursor.getColumnIndex(COLUMN_VOTE)));
            String poster = (cursor.getString(cursor.getColumnIndex(COLUMN_POSTER)));
            String backdrop = (cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP)));
            Long id = (cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));

            Movie movie = new Movie(movieId, title, date, synopsis, vote, poster, backdrop, id);
            movies.add(movie);
        }

        cursor.close();
        return movies;

    }

    private static class MovieDbHelper extends SQLiteOpenHelper {

        MovieDbHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //Creates table
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_MOVIE);
        }

        //Upgrades table
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(MovieDbHelper.class.getName(), "upgrading database from version " + newVersion + "to" + oldVersion + ",which will destroy all old data");
            db.execSQL("Drop table if exist " + MOVIE_TABLE);
            onCreate(db);
        }
    }

}

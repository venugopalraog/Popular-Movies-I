package udacity.com.popularmovies.common;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.model.MovieVideos;
import udacity.com.popularmovies.model.TrailerInfo;
import udacity.com.popularmovies.model.db.MovieContract.MovieEntry;
import udacity.com.popularmovies.model.db.MovieContract.MovieTrailerEntry;

/**
 * Created by venugopalraog on 10/22/16.
 */

public class MovieConverter {

    public static Movie toMovie(Cursor cursor) {

        Movie movie = new Movie();

        movie.setId(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID_KEY)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)));
        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)));
        movie.setVoteCount(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT)));

        return movie;
    }

    public static TrailerInfo toMovieVideos(Cursor cursor) {

        TrailerInfo trailerInfo = new TrailerInfo();

        trailerInfo.setId(cursor.getString(cursor.getColumnIndex(MovieTrailerEntry.COLUMN_MOVIE_ID_KEY)));
        trailerInfo.setKey(cursor.getString(cursor.getColumnIndex(MovieTrailerEntry.COLUMN_MOVIE_TRAILER_KEY)));
        trailerInfo.setName(cursor.getString(cursor.getColumnIndex(MovieTrailerEntry.COLUMN_MOVIE_NAME)));

        return trailerInfo;
    }

    public static ContentValues[] toMovieVideosContentValues(MovieVideos movieVideos) {

        List<TrailerInfo> trailerInfoList = movieVideos.getTrailerInfoList();

        ContentValues[] contentValues = new ContentValues[trailerInfoList.size()];
        int index = 0;

        for (TrailerInfo trailerInfo : trailerInfoList) {
            ContentValues contentValue = new ContentValues();
            contentValue.put(MovieTrailerEntry.COLUMN_MOVIE_ID_KEY, movieVideos.getId());
            contentValue.put(MovieTrailerEntry.COLUMN_MOVIE_NAME, trailerInfo.getName());
            contentValue.put(MovieTrailerEntry.COLUMN_MOVIE_TRAILER_KEY, trailerInfo.getKey());

            contentValues[index] = contentValue;
            index++;
        }

        return contentValues;
    }

    public static ContentValues toMovieContentValues(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieEntry.COLUMN_MOVIE_ID_KEY, movie.getId());
        contentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieEntry.COLUMN_FAVORITE, "Favorite");
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());

        return contentValues;
    }

}

package udacity.com.popularmovies.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import udacity.com.popularmovies.common.MovieConverter;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.model.MovieVideos;
import udacity.com.popularmovies.model.db.MovieContract.MovieEntry;
import udacity.com.popularmovies.model.db.MovieContract.MovieTrailerEntry;

/**
 * Created by venugopalraog on 10/17/16.
 */
public class MovieDbService extends IntentService {

    private static final String TAG = MovieDbService.class.getSimpleName();

    public static final String INSERT_FAVORITE_MOVIE = "addFavoriteMovie";
    public static final String INSERT_FAVORITE_MOVIE_VIDEOS = "addFavoriteMovieVideos";


    public MovieDbService(String name) {
        super(name);
    }

    public MovieDbService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Movie movie = intent.getParcelableExtra(INSERT_FAVORITE_MOVIE);

        if (movie != null)
            insertMovieEntryData(movie);

        MovieVideos movieVideos = intent.getParcelableExtra(INSERT_FAVORITE_MOVIE_VIDEOS);

        if (movieVideos != null)
            insertMovieVideosData(movieVideos);
    }

    private void insertMovieVideosData(MovieVideos movieVideos) {
        String[] projection = new String[]{MovieTrailerEntry.COLUMN_MOVIE_ID_KEY};
        String selection = MovieTrailerEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + movieVideos.getId();

        Cursor movieCursor = getApplicationContext().getContentResolver().query(MovieTrailerEntry.CONTENT_URI,
                projection, selection, selectionArgs, null);

        if (movieCursor != null) {
            if(!movieCursor.moveToFirst()) {
                getApplicationContext().getContentResolver().bulkInsert(MovieTrailerEntry.CONTENT_URI,
                        MovieConverter.toMovieVideosContentValues(movieVideos));
            }
        }
    }

    private void insertMovieEntryData(Movie movie) {
        String[] projection = new String[]{MovieEntry.COLUMN_MOVIE_ID_KEY};
        String selection = MovieEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + movie.getId();

        Cursor movieCursor = getApplicationContext().getContentResolver().query(MovieEntry.CONTENT_URI,
                                            projection, selection, selectionArgs, null);

        if (movieCursor != null) {
            if(!movieCursor.moveToFirst()) {
                getApplicationContext().getContentResolver().insert(MovieEntry.CONTENT_URI,
                        MovieConverter.toMovieContentValues(movie));
            }
        }
    }

}

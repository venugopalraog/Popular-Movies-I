package udacity.com.popularmovies.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

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

    public static final int OPERATION_ADD = 101;
    public static final int OPERATION_REMOVE = 102;

    public static final String OPERATION_MOVIE = "operationMovie";
    public static final String FAVORITE_MOVIE = "movie_data";
    public static final String FAVORITE_MOVIE_VIDEOS = "movie_videos_data";
    public static final String FAVORITE_MOVIE_ID = "movie_id";


    public MovieDbService(String name) {
        super(name);
    }

    public MovieDbService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int operation = intent.getIntExtra(OPERATION_MOVIE, 0);

        switch (operation) {
            case OPERATION_ADD:
                insertMovieEntryData((Movie) intent.getParcelableExtra(FAVORITE_MOVIE));
                insertMovieVideosData((MovieVideos) intent.getParcelableExtra(FAVORITE_MOVIE_VIDEOS));
                break;
            case OPERATION_REMOVE:
                deleteMovieEntryData(intent.getStringExtra(FAVORITE_MOVIE_ID));
                deleteMovieVideosData(intent.getStringExtra(FAVORITE_MOVIE_ID));
                break;
            default:
                Log.d(TAG, " Invalid Operation Received ");
        }
    }

    private void deleteMovieVideosData(String movieId) {
        String selection = MovieTrailerEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + movieId;

        int result = getApplicationContext().getContentResolver().delete(MovieTrailerEntry.CONTENT_URI, selection, selectionArgs);

        Log.d(TAG, " Deleted Movie with Id:: " + movieId + " result:: " + result);
    }

    private void deleteMovieEntryData(String movieId) {
        String selection = MovieTrailerEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + movieId;

        int result = getApplicationContext().getContentResolver().delete(MovieEntry.CONTENT_URI, selection, selectionArgs);

        Log.d(TAG, " Deleted Movie with Id:: " + movieId + " result:: " + result);
    }

    private void insertMovieVideosData(MovieVideos movieVideos) {
        if (movieVideos == null) return;

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
        if (movie == null) return;

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

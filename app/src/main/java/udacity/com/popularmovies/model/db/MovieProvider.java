package udacity.com.popularmovies.model.db;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import udacity.com.popularmovies.model.db.MovieContract.MovieEntry;
import udacity.com.popularmovies.model.db.MovieContract.MovieTrailerEntry;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    static final int MOVIES = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int MOVIE_VIDEOS_WITH_ID = 102;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*", MOVIE_VIDEOS_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, MOVIE_VIDEOS_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES:
            case MOVIE_WITH_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_VIDEOS_WITH_ID:
                return MovieTrailerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor result;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
            case MOVIE_WITH_ID:
                result = mMovieDbHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_VIDEOS_WITH_ID:
                result = mMovieDbHelper.getReadableDatabase().query(MovieTrailerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
            case MOVIE_WITH_ID: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case MOVIE_VIDEOS_WITH_ID: {
                long _id = db.insert(MovieTrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES:
            case MOVIE_WITH_ID:
                return insertBulkData(MovieEntry.TABLE_NAME, uri, values, db);
            case MOVIE_VIDEOS_WITH_ID:
                return insertBulkData(MovieTrailerEntry.TABLE_NAME, uri, values, db);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private int insertBulkData(String table, Uri uri, ContentValues[] values, SQLiteDatabase database) {
        int returnCount = 0;
        database.beginTransaction();
        try {
            for (ContentValues value : values) {
                long id = database.insert(table, null, value);
                if (id != -1)
                    returnCount++;
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_WITH_ID:
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_VIDEOS_WITH_ID:
                rowsDeleted = db.delete(MovieTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_WITH_ID:
                rowsUpdated = db.update(MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_VIDEOS_WITH_ID:
                rowsUpdated = db.update(MovieTrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMovieDbHelper.close();
        super.shutdown();
    }
}
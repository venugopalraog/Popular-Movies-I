package udacity.com.popularmovies.model.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gubbave on 10/5/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "udacity.com.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";


    /* Inner class that defines the table contents of the Movie table */
    public static final class MovieTrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "movieTrailer";

        public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

        public static final String COLUMN_MOVIE_NAME = "title_key";

        public static final String COLUMN_MOVIE_TRAILER_KEY = "trailer_key";

        public static Uri buildMovieTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the Movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Movie Id
        public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

        // Movie Overview
        public static final String COLUMN_OVERVIEW = "overview";

        // Movie Release Date
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // Movie Poster Path
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // Movie Original title and title
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        // Movie vote count
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        // Movie vote Average
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // Movie Favorite
        public static final String COLUMN_FAVORITE = "favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoriteMovie(String favoriteMovie) {
            return CONTENT_URI.buildUpon().appendPath(favoriteMovie).build();
        }

    }
}

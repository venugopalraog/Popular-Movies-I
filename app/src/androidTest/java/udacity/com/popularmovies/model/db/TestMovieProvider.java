package udacity.com.popularmovies.model.db;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

import udacity.com.popularmovies.model.db.MovieContract.MovieEntry;
import udacity.com.popularmovies.model.db.MovieContract.MovieTrailerEntry;

public class TestMovieProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                MovieTrailerEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from MovieEntry table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MovieTrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from MovieTrailerEntry table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        long movieId = 12345;
        type = mContext.getContentResolver().getType(
                MovieEntry.buildMovieUri(movieId));

        assertEquals("Error: the MovieEntry CONTENT_URI with location should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(
                MovieTrailerEntry.buildMovieTrailerUri(movieId));

        assertEquals("Error: the MovieTrailerEntry CONTENT_URI with location and date should return MovieTrailerEntry.CONTENT_ITEM_TYPE",
                MovieTrailerEntry.CONTENT_ITEM_TYPE, type);
    }
}

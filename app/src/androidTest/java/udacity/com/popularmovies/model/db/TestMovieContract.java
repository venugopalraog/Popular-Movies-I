package udacity.com.popularmovies.model.db;

import android.net.Uri;
import android.test.AndroidTestCase;


public class TestMovieContract extends AndroidTestCase {

    private static final String TEST_MOVIE_ENTRY = "favorite";


    public void testBuildMovie() {
        Uri movieUri = MovieContract.MovieEntry.buildFavoriteMovie("favorite");
        assertNotNull("Error: Null Uri returned.  You must fill-in buildWeatherLocation in " +
                        "MovieContract.",
                movieUri);
        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_MOVIE_ENTRY, movieUri.getLastPathSegment());
        assertEquals("Error: Weather location Uri doesn't match our expected result",
                movieUri.toString(),
                "content://udacity.com.popularmovies/movie/favorite");
    }
}

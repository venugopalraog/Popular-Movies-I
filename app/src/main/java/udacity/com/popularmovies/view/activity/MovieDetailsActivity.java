package udacity.com.popularmovies.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.view.fragment.MovieDetailsFragment;

/**
 * Created by gubbave on 9/26/2016.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_DATA);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, MovieDetailsFragment.newInstance(movie), MovieDetailsFragment.class.getSimpleName())
                    .commit();
        }
    }
}

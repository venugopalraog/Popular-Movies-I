package udacity.com.popularmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.view.fragment.MovieDetailsFragment;
import udacity.com.popularmovies.view.fragment.MovieGridFragment;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.Callback{

    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailsFragment(), MovieDetailsFragment.class.getSimpleName())
                        .commit();
            }
/*
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, new MovieGridFragment(), MovieGridFragment.class.getSimpleName())
                        .commit();
            }
*/
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClicked(Movie movie) {
        if (mTwoPane == true) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, MovieDetailsFragment.newInstance(movie), MovieDetailsFragment.class.getSimpleName())
                    .commit();
        } else {
            Intent detailsIntent = new Intent(this, MovieDetailsActivity.class);
            detailsIntent.putExtra(Constants.MOVIE_DETAILS_DATA, movie);
            startActivity(detailsIntent);
        }

    }
}

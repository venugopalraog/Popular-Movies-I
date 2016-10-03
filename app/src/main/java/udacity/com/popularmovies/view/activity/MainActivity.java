package udacity.com.popularmovies.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.view.fragment.MovieListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.mainContainer, new MovieListFragment(), MovieListFragment.class.getSimpleName())
                                   .commit();

        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

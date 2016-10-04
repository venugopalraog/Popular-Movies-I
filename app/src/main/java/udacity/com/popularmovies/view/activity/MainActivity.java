package udacity.com.popularmovies.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.view.fragment.MovieListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, new MovieListFragment(), MovieListFragment.class.getSimpleName())
                    .commit();
        }
    }

}

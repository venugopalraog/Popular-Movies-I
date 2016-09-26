package udacity.com.popularmovies.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.network.NetworkRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkRequest.getMovieModel();
    }
}

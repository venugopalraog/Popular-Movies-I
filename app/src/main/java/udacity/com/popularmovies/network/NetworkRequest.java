package udacity.com.popularmovies.network;

import android.util.Log;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacity.com.popularmovies.BuildConfig;
import udacity.com.popularmovies.model.PopularMovies;
import udacity.com.popularmovies.event.NetworkSucessEvent;

/**
 * Created by venugopalraog on 9/25/16.
 */

public class NetworkRequest {

    public static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String TAG = NetworkRequest.class.getSimpleName();



    private static PopularMovieInterface createPopularMovieInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        return retrofit.create(PopularMovieInterface.class);
    }

    public static void getMovieModel() {
        Call<PopularMovies> call = createPopularMovieInterface().getMovieModel("popular", BuildConfig.MOVIE_DB_KEY);

        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                Log.d(TAG, "Received server response...");
                Log.d(TAG, " response:: " + response.body().toString());
                EventBus.getDefault().post(new NetworkSucessEvent(response.body()));
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
                Log.d(TAG, "Received server Error response..." + t.getMessage());
            }
        });
    }

    public static void getTopRatedMovieModel() {
        Call<PopularMovies> call = createPopularMovieInterface().getMovieModel("top_rated", BuildConfig.MOVIE_DB_KEY);

        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                Log.d(TAG, "Received server response...");
                Log.d(TAG, " response:: " + response.body().toString());
                EventBus.getDefault().post(new NetworkSucessEvent(response.body()));
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
                Log.d(TAG, "Received server Error response..." + t.getMessage());
            }
        });
    }

}

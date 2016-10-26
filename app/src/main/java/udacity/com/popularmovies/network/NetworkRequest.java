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
import udacity.com.popularmovies.common.event.NetworkFailureEvent;
import udacity.com.popularmovies.common.event.NetworkSucessEvent;
import udacity.com.popularmovies.model.BaseModel;

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

    private static <T extends BaseModel> void requestServer(Call<T> call) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.d(TAG, "Received server response...");
                Log.d(TAG, " response:: " + response.body().toString());
                EventBus.getDefault().post(new NetworkSucessEvent(response.body()));
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                String errorMsg = "Server Error" + t.getMessage();
                EventBus.getDefault().post(new NetworkFailureEvent(errorMsg));
            }
        });
    }

    public static void getPopularMovieModel() {
        requestServer(createPopularMovieInterface().getMovieModel("popular", BuildConfig.MOVIE_DB_KEY));
    }

    public static void getTopRatedMovieModel() {
        requestServer(createPopularMovieInterface().getMovieModel("top_rated", BuildConfig.MOVIE_DB_KEY));
    }

    public static void getMovieTrailerModel(String movieId) {
        requestServer(createPopularMovieInterface().getVideosModel(movieId, BuildConfig.MOVIE_DB_KEY));
    }
}

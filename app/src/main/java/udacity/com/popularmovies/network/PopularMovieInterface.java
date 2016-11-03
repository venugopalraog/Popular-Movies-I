package udacity.com.popularmovies.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.model.MovieReviews;
import udacity.com.popularmovies.model.MovieVideos;
import udacity.com.popularmovies.model.PopularMovies;

/**
 * Created by venugopalraog on 9/25/16.
 */

public interface PopularMovieInterface {

    @GET("3/movie/{categories}")
    Call<List<Movie>> getMoviesList(@Path("categories") String categories, @Query("api_key") String apiKey);

    @GET("3/movie/{categories}")
    Call<PopularMovies> getMovieModel(@Path("categories") String categories, @Query("api_key") String apiKey);

    @GET("3/movie/{movieId}/videos")
    Call<MovieVideos> getVideosModel(@Path("movieId") String categories, @Query("api_key") String apiKey);

    @GET("3/movie/{movieId}/reviews")
    Call<MovieReviews> getReviewsModel(@Path("movieId") String categories, @Query("api_key") String apiKey);

}

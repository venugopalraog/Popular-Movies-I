package udacity.com.popularmovies.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import butterknife.Unbinder;
import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.common.MovieConverter;
import udacity.com.popularmovies.common.event.NetworkFailureEvent;
import udacity.com.popularmovies.common.event.NetworkSucessEvent;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.model.MovieReviews;
import udacity.com.popularmovies.model.MovieVideos;
import udacity.com.popularmovies.model.TrailerInfo;
import udacity.com.popularmovies.model.db.MovieContract;
import udacity.com.popularmovies.network.NetworkRequest;
import udacity.com.popularmovies.service.MovieDbService;
import udacity.com.popularmovies.view.adapter.OnItemClickListener;
import udacity.com.popularmovies.view.adapter.ReviewListAdapter;
import udacity.com.popularmovies.view.adapter.TrailerListAdapter;

/**
 * Created by gubbave on 9/26/2016.
 */

public class MovieDetailsFragment extends Fragment implements OnItemClickListener {

    private Movie mMovie;
    private MovieReviews mMovieReviews;
    private MovieVideos mMovieVideos;

    private Unbinder mUnbinder;
    private TrailerListAdapter mTrailerListAdapter;
    private ReviewListAdapter mReviewListAdapter;

    @BindView(R.id.fragment_movie_details_title)        TextView mTitleTv;
    @BindView(R.id.fragment_movie_details_reviewTxt)        TextView mReviewTitle;
    @BindView(R.id.fragment_movie_details_trailerTxt)        TextView mTrailerTitle;
    @BindView(R.id.fragment_movie_details_image)        ImageView mMoviePosterIv;
    @BindView(R.id.fragment_movie_details_time)         TextView mTimeTv;
    @BindView(R.id.fragment_movie_details_ratingBar)       RatingBar mRatingBar;
    @BindView(R.id.fragment_movie_details_rating)       TextView mRatingTv;
    @BindView(R.id.fragment_movie_details_description)  TextView mDescriptionTv;
    @BindView(R.id.fragment_movie_details_trailerList)  RecyclerView mTrailerList;
    @BindView(R.id.fragment_movie_details_reviewList)  RecyclerView mReviewList;

    public static final String YOUTUBE_APP_PATH = "vnd.youtube:";
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";


    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MOVIE_DATA, movie);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.mMovie = getArguments().getParcelable(Constants.MOVIE_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        setRetainInstance(true);
        setHasOptionsMenu(false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(Constants.MOVIE_DATA);
            mMovieVideos = savedInstanceState.getParcelable(Constants.MOVIE_TRAILER_DATA);
        }
        loadScreenData(mMovie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.MOVIE_DATA, mMovie);
        outState.putParcelable(Constants.MOVIE_TRAILER_DATA, mMovieVideos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }




    @OnTouch(R.id.fragment_movie_details_ratingBar)
    public boolean onRatingTouch(View view, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mRatingBar.getRating() == 0) {
                mRatingBar.setRating(1);
                saveFavouriteMovie();
            } else {
                mRatingBar.setRating(0);
                deleteFavoriteMovie(mMovie.getId());
            }
        }
        return true;
    }

    private void deleteFavoriteMovie(String id) {
        Intent intent = new Intent(getActivity(), MovieDbService.class);
        intent.putExtra(MovieDbService.OPERATION_MOVIE, MovieDbService.OPERATION_REMOVE);
        intent.putExtra(MovieDbService.FAVORITE_MOVIE_ID, id);
        getActivity().startService(intent);

    }

    private void saveFavouriteMovie() {
        Intent intent = new Intent(getActivity(), MovieDbService.class);
        intent.putExtra(MovieDbService.OPERATION_MOVIE, MovieDbService.OPERATION_ADD);
        intent.putExtra(MovieDbService.FAVORITE_MOVIE, mMovie);
        intent.putExtra(MovieDbService.FAVORITE_MOVIE_VIDEOS, mMovieVideos);
        getActivity().startService(intent);
    }

    private void loadScreenData(Movie movie) {
        if (movie == null) return;
        mDescriptionTv.setText(movie.getOverview());
        mTitleTv.setText(movie.getOriginalTitle());
        mRatingTv.setText(movie.getVoteAverage() + "/10");
        mTimeTv.setText(movie.getReleaseDate());

        Picasso.with(getContext())
                .load(Constants.MOVIE_DETAILS_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(mMoviePosterIv);


        loadTrailerList(movie);
        loadReviewList(movie);
    }

    private void loadReviewList(Movie movie) {
        mReviewListAdapter = new ReviewListAdapter(getActivity());
        mReviewList.setAdapter(mReviewListAdapter);
        mReviewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        NetworkRequest.getMovieReviewsModel(mMovie.getId());
    }

    private void loadTrailerList(Movie movie) {
        mTrailerListAdapter = new TrailerListAdapter(getActivity());
        mTrailerListAdapter.setListener(this);
        mTrailerList.setAdapter(mTrailerListAdapter);
        mTrailerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (mMovieVideos != null) {
            mTrailerListAdapter.setTrailerInfoList(mMovieVideos.getTrailerInfoList());
            mTrailerListAdapter.notifyDataSetChanged();
        } else if (isTrailerAvailableInDb(mMovie.getId()))
            fetchTrailerDb();
        else
            NetworkRequest.getMovieTrailerModel(movie.getId());

    }

    private void fetchTrailerDb() {
        final String[] MOVIE_TRAILER_COLUMNS = {
                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID_KEY,
                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_NAME,
                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_TRAILER_KEY
        };

        String selection = MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + mMovie.getId();

        Cursor movieTrailerCursor = getActivity().getContentResolver().query(MovieContract.MovieTrailerEntry.CONTENT_URI,
                                                            MOVIE_TRAILER_COLUMNS,
                                                            selection,
                                                            selectionArgs,
                                                            null);
        if (movieTrailerCursor == null) return;

        List<TrailerInfo> trailerInfoList = new ArrayList<>();

        while (movieTrailerCursor.moveToNext()) {
            TrailerInfo trailerInfo = MovieConverter.toMovieVideos(movieTrailerCursor);
            trailerInfoList.add(trailerInfo);
        }
        movieTrailerCursor.close();

        mRatingBar.setRating(1);
        if (trailerInfoList.size() > 0) {
            mTrailerTitle.setVisibility(View.VISIBLE);
            mTrailerListAdapter.setTrailerInfoList(trailerInfoList);
            mTrailerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean isTrailerAvailableInDb(String movieId) {
        String[] projection = new String[]{MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID_KEY};
        String selection = MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID_KEY + "=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = "" + movieId;

        Cursor movieCursor = getActivity().getContentResolver().query(MovieContract.MovieTrailerEntry.CONTENT_URI,
                projection, selection, selectionArgs, null);

        return movieCursor != null && movieCursor.moveToFirst();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkSucessEvent event) {
        if (event.getData() instanceof MovieVideos) {
            mMovieVideos = (MovieVideos) event.getData();

            //Update the Trailer List
            if (mMovieVideos.getTrailerInfoList().size() > 0) {
                mTrailerTitle.setVisibility(View.VISIBLE);
                mTrailerListAdapter.setTrailerInfoList(mMovieVideos.getTrailerInfoList());
                mTrailerListAdapter.notifyDataSetChanged();
            }

        } else if (event.getData() instanceof MovieReviews) {
            mMovieReviews = (MovieReviews) event.getData();

            //Movie contains Review List update the list
            if (mMovieReviews.getResults().size() > 0) {
                mReviewTitle.setVisibility(View.VISIBLE);
                mReviewListAdapter.setReviewInfoList(mMovieReviews.getResults());
                mReviewListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkFailureEvent event) {
        Toast.makeText(getActivity(), "Network Error ::" + event.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int pos) {
        TrailerInfo trailerInfo = mTrailerListAdapter.getItem(pos);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_PATH + trailerInfo.getKey()));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailerInfo.getKey()));
            startActivity(intent);
        }
    }
}

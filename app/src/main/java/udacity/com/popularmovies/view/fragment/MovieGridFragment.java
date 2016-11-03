package udacity.com.popularmovies.view.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.CommonUtils;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.common.MovieConverter;
import udacity.com.popularmovies.common.event.NetworkFailureEvent;
import udacity.com.popularmovies.common.event.NetworkSucessEvent;
import udacity.com.popularmovies.model.Movie;
import udacity.com.popularmovies.model.PopularMovies;
import udacity.com.popularmovies.model.db.MovieContract;
import udacity.com.popularmovies.model.db.MovieContract.MovieEntry;
import udacity.com.popularmovies.network.NetworkRequest;
import udacity.com.popularmovies.view.adapter.PopularMoviesAdapter;


/**
 * Created by gubbave on 9/26/2016.
 */
public class MovieGridFragment extends Fragment implements AdapterView.OnItemClickListener  {   //, LoaderManager.LoaderCallbacks<Cursor> {

/*    public static final int STATE_POPULAR_MOVIE = 101;
    public static final int STATE_TOP_RATED_MOVIE = 102;
    public static final int STATE_FAVORITE_MOVIE = 103;*/

    private PopularMovies mPopularMovies;
//    private List<Movie> mFavoriteMovieList;

    private int mSelectedItem = 0;


    @BindView(R.id.fragment_popular_movie_grid_view) GridView mGridView;
    @BindView(R.id.fragment_popular_movie_infoTxt)   TextView mInfoText;

    private PopularMoviesAdapter mMovieAdapter;
    private Dialog dialog;
    private Unbinder mUnbinder;

//    private int mState = STATE_POPULAR_MOVIE;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPopularMovies == null && CommonUtils.isConnected(getActivity())) {
            NetworkRequest.getPopularMovieModel();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        EventBus.getDefault().register(this);
        mUnbinder = ButterKnife.bind(this, view);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load Favorite Data From Db Table..
//        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mPopularMovies = savedInstanceState.getParcelable(Constants.MOVIE_DATA);
//            mState = savedInstanceState.getInt(Constants.MOVIE_LIST_FRAGMENT_STATE);
        }

        loadScreenData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.MOVIE_DATA, mPopularMovies);
//        outState.putInt(Constants.MOVIE_LIST_FRAGMENT_STATE, mState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkSucessEvent event) {
        if (event.getData() instanceof PopularMovies) {
            mPopularMovies = (PopularMovies) event.getData();
            hideProgressBar();
            if (mPopularMovies != null) {
                updateMovieAdapterList(mPopularMovies.getMovies());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkFailureEvent event) {
        hideProgressBar();
        setInfoText(event.getErrorMsg());
    }

    private void setInfoText(String text) {
        mInfoText.setText(text);
        mInfoText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mSelectedItem = position;
        ((Callback)getActivity()).onItemClicked(mPopularMovies.getMovies().get(mSelectedItem));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular:
                mSelectedItem = 0;
                createProgressBarDialogFragment();
                NetworkRequest.getPopularMovieModel();
                break;
            case R.id.top_rated:
                mSelectedItem = 0;
                createProgressBarDialogFragment();
                NetworkRequest.getTopRatedMovieModel();
                break;
            case R.id.favorite:
                mSelectedItem = 0;
                getFavoriteMovies();
                break;
            case R.id.favorite_delete_all:
                mSelectedItem = 0;
                deleteAllFavorite();
                break;
            default:
                return false;
        }

        return false;
    }

    private void deleteAllFavorite() {

        //Delete the Favorite Data from Database...
        getActivity().getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
        getActivity().getContentResolver().delete(MovieContract.MovieTrailerEntry.CONTENT_URI, null, null);

        mPopularMovies.getMovies().clear();
        updateMovieAdapterList(mPopularMovies.getMovies());
    }

    private void loadScreenData() {
        mMovieAdapter = new PopularMoviesAdapter(getActivity());
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(this);

        /*if (mState == STATE_FAVORITE_MOVIE) {
            mMovieAdapter.setPopularMovieList(mFavoriteMovieList);
            mMovieAdapter.notifyDataSetChanged();
        } else*/

        if (mPopularMovies != null) {
            updateMovieAdapterList(mPopularMovies.getMovies());
        } else {
            mInfoText.setText(R.string.movie_list_no_network_error);
            mInfoText.setVisibility(View.VISIBLE);
        }
    }

    private void createProgressBarDialogFragment() {
        dialog = new Dialog(getActivity(), R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_progressbar);
        dialog.show();
    }

    private void hideProgressBar() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void getFavoriteMovies() {

        final String[] MOVIE_COLUMNS = {
                MovieEntry.COLUMN_MOVIE_ID_KEY,
                MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_POSTER_PATH,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_VOTE_COUNT,
        };

        Cursor movieCursor = getActivity().getContentResolver().query(MovieEntry.CONTENT_URI, MOVIE_COLUMNS, null, null, null);
        if (movieCursor == null) return;

        List<Movie> movies = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            Movie movie = MovieConverter.toMovie(movieCursor);
            movies.add(movie);
        }
        movieCursor.close();

        hideProgressBar();
        mPopularMovies.setMovies(movies);
        updateMovieAdapterList(mPopularMovies.getMovies());
    }

    private void updateMovieAdapterList(List<Movie> movies) {
        if (movies.size() > 0) {
            mInfoText.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mMovieAdapter.setPopularMovieList(movies);
            mMovieAdapter.notifyDataSetChanged();
            ((Callback) getActivity()).onRefresDetailView(mPopularMovies.getMovies().get(mSelectedItem));
        } else {
            mGridView.setVisibility(View.GONE);
            setInfoText(getContext().getString(R.string.movie_list_no_data_error));
        }
    }

/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final String[] MOVIE_COLUMNS = {
                MovieEntry.COLUMN_MOVIE_ID_KEY,
                MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieEntry.COLUMN_OVERVIEW,
                MovieEntry.COLUMN_POSTER_PATH,
                MovieEntry.COLUMN_RELEASE_DATE,
                MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieEntry.COLUMN_VOTE_COUNT,
        };

        return new CursorLoader(getActivity(), MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor movieCursor) {

        if (movieCursor.getCount() > 0) {
            mFavoriteMovieList = new ArrayList<>();

            movieCursor.moveToFirst();

            while (movieCursor.moveToNext()) {
                Movie movie = MovieConverter.toMovie(movieCursor);
                mFavoriteMovieList.add(movie);
            }

            if (mState == STATE_FAVORITE_MOVIE) {
                mMovieAdapter.setPopularMovieList(mFavoriteMovieList);
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
*/

    public interface Callback {
        void onItemClicked(Movie movie);
        void onRefresDetailView(Movie movie);
    }
}

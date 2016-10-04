package udacity.com.popularmovies.view.fragment;

import android.app.Dialog;
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

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.CommonUtils;
import udacity.com.popularmovies.event.NetworkFailureEvent;
import udacity.com.popularmovies.event.NetworkSucessEvent;
import udacity.com.popularmovies.model.PopularMovies;
import udacity.com.popularmovies.network.NetworkRequest;
import udacity.com.popularmovies.view.adapter.PopularMoviesAdapter;


/**
 * Created by gubbave on 9/26/2016.
 */

public class MovieListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static final int POPULAR_ITEM = 1;
    private static final int TOP_RATED_ITEM = 2;

    private int mSelectedSortOrder = POPULAR_ITEM;
    private PopularMovies mPopularMovies;

    private GridView mGridView;
    private TextView mInfoText;
    private PopularMoviesAdapter mMovieAdapter;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        mGridView = (GridView) view.findViewById(R.id.fragment_popular_movie_grid_view);
        mInfoText = (TextView) view.findViewById(R.id.fragment_popular_movie_infoTxt);

        mMovieAdapter = new PopularMoviesAdapter(getActivity());
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(this);

        if (CommonUtils.isConnected(getActivity())) {
            createProgressBarDialogFragment();
            NetworkRequest.getMovieModel();
        } else {
            mInfoText.setText(R.string.movie_list_no_network_error);
            mInfoText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkSucessEvent event) {
        mPopularMovies = (PopularMovies) event.getData();
        hideProgressBar();
        if (mPopularMovies != null) {
            mMovieAdapter.setPopularMovieList(mPopularMovies.getMovies());
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NetworkFailureEvent event) {
        hideProgressBar();
        mInfoText.setText(event.getErrorMsg());
        mInfoText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(MovieDetailsFragment.class.getSimpleName())
                .replace(R.id.mainContainer, MovieDetailsFragment.newInstance(mPopularMovies.getMovies().get(position)))
                .commit();
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
                handleOptionMenuClicked(POPULAR_ITEM);
                break;
            case R.id.top_rated:
                handleOptionMenuClicked(TOP_RATED_ITEM);
                break;
            default:
                return false;
        }

        return false;
    }

    private void handleOptionMenuClicked(int menuItem) {
        mSelectedSortOrder = menuItem;
        switch (menuItem) {
            case POPULAR_ITEM:
                createProgressBarDialogFragment();
                NetworkRequest.getMovieModel();
                break;
            case TOP_RATED_ITEM:
                createProgressBarDialogFragment();
                NetworkRequest.getTopRatedMovieModel();
                break;
            default:
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
}

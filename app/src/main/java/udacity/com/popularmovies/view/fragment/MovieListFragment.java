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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

    private PopularMovies mPopularMovies;

    @BindView(R.id.fragment_popular_movie_grid_view) GridView mGridView;
    @BindView(R.id.fragment_popular_movie_infoTxt)   TextView mInfoText;

    private PopularMoviesAdapter mMovieAdapter;
    private Dialog dialog;
    private Unbinder mUnbinder;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadScreenData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
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
                createProgressBarDialogFragment();
                NetworkRequest.getMovieModel();
                break;
            case R.id.top_rated:
                createProgressBarDialogFragment();
                NetworkRequest.getTopRatedMovieModel();
                break;
            default:
                return false;
        }

        return false;
    }

    private void loadScreenData() {
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

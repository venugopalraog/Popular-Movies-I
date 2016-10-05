package udacity.com.popularmovies.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.model.Movie;

/**
 * Created by gubbave on 9/26/2016.
 */

public class MovieDetailsFragment extends Fragment {

    private Movie mMovie;
    private TextView mTitleTv;
    private ImageView mMoviePosterIv;
    private TextView mTimeTv;
    private TextView mRatingTv;
    private Button mFavouriteBtn;
    private TextView mDescriptionTv;


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
        setRetainInstance(true);
        setHasOptionsMenu(false);
        this.mMovie = getArguments().getParcelable(Constants.MOVIE_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        loadScreenData(mMovie);
    }

    private void initView(View view) {
        mTitleTv = (TextView) view.findViewById(R.id.fragment_movie_details_title);
        mMoviePosterIv = (ImageView) view.findViewById(R.id.fragment_movie_details_image);
        mTimeTv = (TextView) view.findViewById(R.id.fragment_movie_details_time);
        mRatingTv = (TextView) view.findViewById(R.id.fragment_movie_details_rating);
        mFavouriteBtn = (Button) view.findViewById(R.id.fragment_movie_details_favourite);
        mDescriptionTv = (TextView) view.findViewById(R.id.fragment_movie_details_description);
    }

    private void loadScreenData(Movie movie) {
        mDescriptionTv.setText(movie.getOverview());
        mTitleTv.setText(movie.getOriginalTitle());
        mRatingTv.setText(movie.getVoteAverage() + "/10");
        mTimeTv.setText(movie.getReleaseDate());

        Picasso.with(getContext())
                .load(Constants.MOVIE_DETAILS_IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(mMoviePosterIv);
    }
}

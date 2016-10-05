package udacity.com.popularmovies.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.model.Movie;

/**
 * Created by gubbave on 9/26/2016.
 */

public class PopularMoviesAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mPopularMovieList;


    public PopularMoviesAdapter(Context context, List<Movie> popularMovieList) {
        this.mContext = context;
        this.mPopularMovieList = popularMovieList;
    }

    public PopularMoviesAdapter(Context context) {
        this.mContext = context;
    }

    public void setPopularMovieList(List<Movie> popularMovieList) {
        this.mPopularMovieList = popularMovieList;
    }

    @Override
    public int getCount() {
        if (mPopularMovieList != null)
            return mPopularMovieList.size();
        return 0;
    }

    @Override
    public Movie getItem(int pos) {
        return mPopularMovieList.get(pos);
    }

    @Override
    public long getItemId(int viewType) {
        return viewType;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if (view != null && view instanceof ImageView) {
            imageView = (ImageView) view;
        } else {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        }

        Picasso.with(mContext)
                .load(Constants.MOVIE_GRID_VIEW_IMAGE_URL + getItem(pos).getPosterPath())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_errorloading)
                .into(imageView);

        return imageView;
    }
}

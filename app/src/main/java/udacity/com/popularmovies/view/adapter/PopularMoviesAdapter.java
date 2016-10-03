package udacity.com.popularmovies.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.com.popularmovies.model.Movie;

/**
 * Created by gubbave on 9/26/2016.
 */

public class PopularMoviesAdapter extends BaseAdapter {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
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

        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) view;
        }

        Picasso.with(mContext)
               .load(IMAGE_URL + getItem(pos).getPosterPath())
               .into(imageView);

        return imageView;
    }
}

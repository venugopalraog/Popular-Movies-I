
package udacity.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PopularMovies implements Parcelable{

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> movies = new ArrayList<Movie>();

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    protected PopularMovies(Parcel in) {
        page = in.readInt();
        movies = in.createTypedArrayList(Movie.CREATOR);
        totalResults = in.readInt();
        totalPages = in.readInt();
    }

    public static final Creator<PopularMovies> CREATOR = new Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    public int getPage() {
        return page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(movies);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
    }
}

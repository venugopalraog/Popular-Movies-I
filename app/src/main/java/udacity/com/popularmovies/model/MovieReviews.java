package udacity.com.popularmovies.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venugopalraog on 10/28/16.
 */

public class MovieReviews extends BaseModel{

    @SerializedName("id")
    private int id;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("results")
    private List<Review> results;

    protected MovieReviews(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.page = in.readInt();
        this.total_pages = in.readInt();
        this.total_results = in.readInt();
        this.results = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(id);
        dest.writeInt(page);
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
        dest.writeTypedList(results);
    }
}
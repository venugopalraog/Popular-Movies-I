package udacity.com.popularmovies.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * Created by gubbave on 10/5/2016.
 */

public class MovieVideos extends BaseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("results")
    private List<TrailerInfo> trailerInfoList;

    protected MovieVideos(Parcel in) {
        super(in);
        id = in.readString();
        trailerInfoList = in.createTypedArrayList(TrailerInfo.CREATOR);
    }

    public String getId() {
        return id;
    }

    public List<TrailerInfo> getTrailerInfoList() {
        return trailerInfoList;
    }

    public static final Creator<MovieVideos> CREATOR = new Creator<MovieVideos>() {
        @Override
        public MovieVideos createFromParcel(Parcel in) {
            return new MovieVideos(in);
        }

        @Override
        public MovieVideos[] newArray(int size) {
            return new MovieVideos[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeTypedList(trailerInfoList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieVideos that = (MovieVideos) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(trailerInfoList, that.trailerInfoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trailerInfoList);
    }
}

package udacity.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gubbave on 10/5/2016.
 */

public class BaseModel implements Parcelable {

    public BaseModel() {   }

    protected BaseModel(Parcel in) {    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel in) {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {   }
}

package udacity.com.popularmovies.event;

import android.os.Parcelable;

/**
 * Created by gubbave on 9/26/2016.
 */

public class NetworkSucessEvent {

    private Parcelable mData;

    public NetworkSucessEvent (Parcelable data) {
        this.mData = data;
    }

    public Parcelable getmData() {
        return mData;
    }

    public void setmData(Parcelable mData) {
        this.mData = mData;
    }
}

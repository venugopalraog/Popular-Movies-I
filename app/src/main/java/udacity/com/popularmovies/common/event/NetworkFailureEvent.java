package udacity.com.popularmovies.common.event;

/**
 * Created by gubbave on 10/3/2016.
 */

public class NetworkFailureEvent {

    private String errorMsg;

    public NetworkFailureEvent (String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

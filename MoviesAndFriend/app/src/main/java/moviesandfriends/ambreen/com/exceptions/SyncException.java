package moviesandfriends.ambreen.com.exceptions;

/**
 * Created by ambreen on 11/4/16.
 */

public class SyncException extends Exception {

    public String toString() {
        return "SyncException Occurred";
    }

    public class MovieDBServerException extends Exception {
        public int responseCode;

        public MovieDBServerException(int responseCode) {
            this.responseCode = responseCode;
        }

        public String toString() {
            return "Exception happened while fetching request form MovieDB server. ResponseCode received: "+this.responseCode;
        }


    }
}

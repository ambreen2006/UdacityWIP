package moviesandfriends.ambreen.com.exceptions;

/**
 * Created by ambreen on 11/4/16.
 */

public class SyncException extends Exception {

    public int responseCode;
    public String message;

    public SyncException()
    {}

    public SyncException(String message)
    {
        this.message = message;
    }

    public String toString()
    {
        if(responseCode > 0)
        {
            return message+" Response Code = "+responseCode;
        }
        else
            return message;
    }
}

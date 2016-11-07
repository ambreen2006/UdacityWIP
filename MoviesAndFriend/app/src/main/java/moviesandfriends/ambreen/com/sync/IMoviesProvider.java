package moviesandfriends.ambreen.com.sync;
import java.io.IOException;
import java.net.MalformedURLException;

import moviesandfriends.ambreen.com.exceptions.SyncException;

/**
 * Created by ambreen on 11/4/16.
 */

public interface IMoviesProvider
{
    void getListOfMoviesFilteredBy(String filter) throws SyncException;
}

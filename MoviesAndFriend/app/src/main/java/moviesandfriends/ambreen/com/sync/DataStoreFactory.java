package moviesandfriends.ambreen.com.sync;

/**
 * Created by ambreen on 11/7/16.
 */

public final class DataStoreFactory
{
    public static LocalDataStore<MovieData> localDataStore = new LocalDataStore<MovieData>();

    public static LocalDataStore<MovieData> getMovieDataStore()
    {
        return localDataStore;
    }
}

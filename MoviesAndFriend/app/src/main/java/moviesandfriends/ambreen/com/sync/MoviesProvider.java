package moviesandfriends.ambreen.com.sync;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.moviesandfriend.BuildConfig;

/**
 * Created by ambreen on 11/4/16.
 */

public class MoviesProvider implements IMoviesProvider {

    private     ClientHttpConnection httpConnection;
    private     LocalDataStore dataStore;
    private     String key;

    public MoviesProvider(ClientHttpConnection httpConnection)
    {
        this.httpConnection = httpConnection;
        this.dataStore = DataStoreFactory.getMovieDataStore();
        this.key = BuildConfig.MOVIE_DB_KEY;
    }

    public MoviesProvider(Context context)
    {
        this((new ClientHttpConnection(context)));
    }

    public void getListOfMoviesFilteredBy(String filter) throws SyncException {

        InputStream is = null;

        if(!this.httpConnection.isNetworkConnected())
        {
            throw new SyncException("Network connectivity could not be established");
        }

        try
        {
            JSONObject jsonObject = this.httpConnection.fetchContent(getURL(filter));
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject aMovie = results.getJSONObject(i);

                MovieData movieData = new MovieData();

                movieData.title = aMovie.getString(Constants.MovieDB.TITLE_KEY);
                movieData.posterPath = aMovie.getString(Constants.MovieDB.POSTER_PATH_KEY);
                movieData.overview = aMovie.getString(Constants.MovieDB.OVERVIEW_KEY);
                movieData.releaseDate = aMovie.getString(Constants.MovieDB.RELEASE_DATE_KEY);
                movieData.backdropPath = aMovie.getString(Constants.MovieDB.BACKDROP_PATH_KEY);
                movieData.voteAverage = aMovie.getString(Constants.MovieDB.VOTE_AVERAGE_KEY);

                this.dataStore.addData(filter, movieData);
            }

            Log.d(Constants.LogTag.MOVIES_PROVIDER,"results size = "+results.length());
        }
        catch (JSONException | MalformedURLException exception)
        {
            Log.e(Constants.LogTag.MOVIES_PROVIDER,exception.toString());
        }
    }

    public boolean isNetworkConnected()
    {
        return httpConnection.isNetworkConnected();
    }

    private URL getURL(String filter) throws MalformedURLException
    {
        StringBuilder urlBuilder = new StringBuilder(Constants.MovieDB.URL_PREFIX);
        urlBuilder.append(filter);
        urlBuilder.append("?");
        urlBuilder.append("page=1");
        urlBuilder.append("&api_key=");
        urlBuilder.append(key);
        return new URL(urlBuilder.toString());
    }

    public String getStringURLForPoster(String posterPath, int size)
    {
        StringBuilder imageURL = new StringBuilder();
        imageURL.append(Constants.MovieDB.THUMBNAIL_URL_PREFIX).append(size).append(posterPath);
        return imageURL.toString();
    }
}

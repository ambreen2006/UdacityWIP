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

    private     Context context;
    private     ClientHttpConnection httpConnection;
    private     LocalDataStore dataStore;
    private     String key;

    public MoviesProvider(Context context, ClientHttpConnection httpConnection)
    {
        this.context = context;
        this.httpConnection = httpConnection;
        this.dataStore = DataStoreFactory.getMovieDataStore();
    }

    public MoviesProvider(Context context)
    {
        this.context = context;
        this.dataStore = DataStoreFactory.getMovieDataStore();
        this.httpConnection =  new ClientHttpConnection(context);
        this.key = BuildConfig.MOVIE_DB_KEY;
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

                String title = aMovie.getString("title");
                int id = aMovie.getInt("id");

                MovieData movieData = new MovieData();
                movieData.posterPath = aMovie.getString("poster_path");
                movieData.title = aMovie.getString("title");
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
        StringBuilder urlBuilder = new StringBuilder("https://api.themoviedb.org/3/movie/");

        urlBuilder.append(filter);
        urlBuilder.append("?");
        urlBuilder.append("page=1");
        urlBuilder.append("&api_key=");
        urlBuilder.append(key);
        return new URL(urlBuilder.toString());
    }
}

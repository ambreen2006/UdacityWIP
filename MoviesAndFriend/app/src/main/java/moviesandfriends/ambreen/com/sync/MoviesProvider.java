package moviesandfriends.ambreen.com.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import moviesandfriends.ambreen.com.Constants.LogTag;
import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.background.BackgroundService;
import moviesandfriends.ambreen.com.moviesandfriend.BuildConfig;

import static moviesandfriends.ambreen.com.sync.LocalDataStore.*;

/**
 * Created by ambreen on 11/4/16.
 */


public class MoviesProvider implements IMoviesProvider {

    private     Context context;
    private     MovieSourceHttpConnection httpConnection;
    private     LocalDataStore dataStore;
    private     String key;

    public MoviesProvider(Context context, MovieSourceHttpConnection httpConnection)
    {
        this.context = context;
        this.httpConnection = httpConnection;
        this.dataStore = LocalDataStore.getInstance();
    }

    public MoviesProvider(Context context)
    {
        this.context = context;
        this.dataStore = LocalDataStore.getInstance();
        this.httpConnection =  new MovieSourceHttpConnection(context);
        this.key = BuildConfig.MOVIE_DB_KEY;
    }

    public void getListOfMoviesFilteredBy(String filter) throws SyncException {

        InputStream is = null;

        if(!this.httpConnection.isNetworkConnected())
            throw new SyncException();

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
        }
        catch (JSONException | MalformedURLException exception)
        {
            Log.e(LogTag.MOVIES_PROVIDER,exception.toString());
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

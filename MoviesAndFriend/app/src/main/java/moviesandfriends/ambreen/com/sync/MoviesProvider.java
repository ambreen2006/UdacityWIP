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

import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.background.BackgroundService;
import moviesandfriends.ambreen.com.moviesandfriend.BuildConfig;

import static moviesandfriends.ambreen.com.sync.LocalDataStore.*;

/**
 * Created by ambreen on 11/4/16.
 */


public class MoviesProvider implements IMoviesProvider {

    protected  Context context;
    protected  LocalDataStore dataStore;

    private String key;

    public MoviesProvider(Context context)
    {
        this.context = context;
        dataStore = getInstance();
    }

    public void getListOfMoviesFilteredByType(int type) throws SyncException, IOException {

        InputStream is = null;

        if(!isNetworkConnected())
            throw new SyncException();

        try
        {
            MovieSourceHttpConnection connection = new MovieSourceHttpConnection();
            JSONObject jsonObject = connection.fetchContent(getURL());
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject aMovie = results.getJSONObject(i);
                String title = aMovie.getString("title");
                int id = aMovie.getInt("id");
                LocalDataStore.MovieData movieData = dataStore.new MovieData();
                movieData.posterPath = aMovie.getString("poster_path");
                movieData.title = aMovie.getString("title");
                Log.d("MovieProvider", ""+movieData.title+" ID = "+id);
                dataStore.addData(movieData);
            }

        }
        catch (JSONException jException)
        {
            Log.e("MovieProvider","Error parsing json "+jException.toString());
        }
        catch (Exception exception)
        {
            throw  exception;
        }
    }

    protected boolean isNetworkConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if(info != null && info.isConnected())
        {
            return true;
        }

        return false;
    }

    private URL getURL() throws MalformedURLException
    {
        URL url = new URL("https://api.themoviedb.org/3/movie/popular?page=1&api_key=");
        return url;
    }
}

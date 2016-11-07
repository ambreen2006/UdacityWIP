package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.sync.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class MoviesUnitTest
{
    @Mock private Context mContext;
    @Mock private MovieSourceHttpConnection httpConnection;
    @InjectMocks MoviesProvider provider;

    @Test(expected = SyncException.class)
    public void testThrowsSyncException() throws SyncException, MalformedURLException
    {
        when(httpConnection.isNetworkConnected()).thenReturn(false);
        provider.getListOfMoviesFilteredBy("popular");
    }

    @Test
    public void testDataStoreDataForFilterRequest() throws JSONException, SyncException, MalformedURLException
    {
        String filter = "popular";
        JSONObject dummyJson = dummyMovieData1();
        JSONArray array = (JSONArray) dummyJson.get("results");

        when(httpConnection.isNetworkConnected()).thenReturn(true);
        when(httpConnection.fetchContent(any(URL.class))).thenReturn(dummyJson);

        provider.getListOfMoviesFilteredBy(filter);
        LocalDataStore dataStore = LocalDataStore.getInstance();
        MovieData movieData = (MovieData) dataStore.getData(filter,0);

        assertEquals(movieData.posterPath,((JSONObject)array.get(0)).get("poster_path").toString());
        assertEquals(movieData.title,((JSONObject)array.get(0)).get("title").toString());
        assertEquals(1,dataStore.count(filter));
    }

    private static JSONObject dummyMovieData1()  throws JSONException {

        JSONObject movie1 = new JSONObject();
        movie1.put("id",1);
        movie1.put("title","Patch Adams");
        movie1.put("poster_path","https://image.tmdb.org/t/p/original/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg");

        JSONArray resultArray = new JSONArray();
        resultArray.put(movie1);

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("results",resultArray);
        return responseJSON;
    }
}
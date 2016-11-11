package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import moviesandfriends.ambreen.com.constants.Constants;
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
    @Mock private ClientHttpConnection httpConnection;
    @InjectMocks MoviesProvider provider;

    private LocalDataStore dataStore;

    @Before
    public void setUp()
    {
        this.dataStore = DataStoreFactory.getMovieDataStore();
        this.dataStore.removeDataForFilter(Constants.FilterTypeConst.POPULAR);
        this.dataStore.removeDataForFilter(Constants.FilterTypeConst.TOP_RATED);
    }

    @Test(expected = SyncException.class)
    public void testThrowsSyncException() throws SyncException, MalformedURLException
    {
        when(httpConnection.isNetworkConnected()).thenReturn(false);
        provider.getListOfMoviesFilteredBy("popular",1);
    }

    @Test
    public void testDataStoreDataForFilterRequest() throws JSONException, SyncException, MalformedURLException
    {
        String filter = "popular";
        JSONObject dummyJson = dummyMovieData1();
        JSONArray array = (JSONArray) dummyJson.get(Constants.MovieDBConst.RESULTS_KEY);

        when(httpConnection.isNetworkConnected()).thenReturn(true);
        when(httpConnection.fetchContent(any(URL.class))).thenReturn(dummyJson);

        provider.getListOfMoviesFilteredBy(filter,1);

        MovieData movieData = this.dataStore.getData(filter,0);

        assertEquals(movieData.posterPath,((JSONObject)array.get(0)).get(Constants.MovieDBConst.POSTER_PATH_KEY).toString());
        assertEquals(movieData.title,((JSONObject)array.get(0)).get(Constants.MovieDBConst.TITLE_KEY).toString());
        assertEquals(1,dataStore.count(filter));
    }

    @Test
    public void testDataStoreClearContent() throws JSONException, SyncException, MalformedURLException
    {
        String filter = Constants.FilterTypeConst.POPULAR;
        JSONObject dummyJson = dummyMovieData1();

        when(httpConnection.isNetworkConnected()).thenReturn(true);
        when(httpConnection.fetchContent(any(URL.class))).thenReturn(dummyJson);

        provider.getListOfMoviesFilteredBy(filter,1);
        dataStore = DataStoreFactory.getMovieDataStore();

        assertEquals(1,dataStore.count(filter));

        dataStore.removeDataForFilter(filter);
        assertEquals(0,dataStore.count(filter));
    }

    @Test
    public void testMoviesListByFeature()
    {
        LocalDataStore.MovieList moviesList1 = this.dataStore.getMoviesList(Constants.FilterTypeConst.POPULAR);

        String title1 = "Almost Christmas";
        MovieData mData1 = new MovieData(title1,"https://whateverpath.com",new Date(),5.0f,"Test Movie 1","https://whateverpath.com");

        moviesList1.contents.add(mData1);
        moviesList1.lastPage = 1;

        dataStore.setDataList(Constants.FilterTypeConst.POPULAR,moviesList1);

        LocalDataStore.MovieList moviesList2 = this.dataStore.getMoviesList(Constants.FilterTypeConst.TOP_RATED);
        String title2 = "Doctor Strange";
        MovieData mData2 = new MovieData(title2,"https://whateverpath.com",new Date(),5.0f,"Test Movie 1","https://whateverpath.com");
        moviesList2.lastPage = 1;
        moviesList2.contents.add(mData2);

        dataStore.setDataList(Constants.FilterTypeConst.TOP_RATED,moviesList2);

        MovieData storedData = dataStore.getData(Constants.FilterTypeConst.POPULAR,0);
        assertEquals(title1,storedData.title);

        storedData = dataStore.getData(Constants.FilterTypeConst.TOP_RATED,0);
        assertEquals(title2,storedData.title);
    }

    @Test
    public void testGetMovieListReturnsValid()
    {
        LocalDataStore.MovieList movieList = dataStore.getMoviesList("popular");

        assertNotNull(movieList);
        assertNotNull(movieList.contents);
    }

    @Test
    public void testDuplicatePageRequestAreDiscarded() throws SyncException, JSONException
    {
        String filter = Constants.FilterTypeConst.POPULAR;
        JSONObject dummyJson = dummyMovieData1();
        JSONArray array = (JSONArray) dummyJson.get(Constants.MovieDBConst.RESULTS_KEY);

        when(httpConnection.isNetworkConnected()).thenReturn(true);
        when(httpConnection.fetchContent(any(URL.class))).thenReturn(dummyJson);

        provider.getListOfMoviesFilteredBy(filter,1);
        provider.getListOfMoviesFilteredBy(filter,1);
        provider.getListOfMoviesFilteredBy(filter,1);

        assertEquals(1,this.dataStore.count(filter));
    }

    @Test
    public void testExceededPageRequestsAreDiscarded() throws SyncException, JSONException
    {
        String filter = Constants.FilterTypeConst.POPULAR;
        JSONObject dummyJson = dummyMovieData2();
        //JSONArray array = (JSONArray) dummyJson.get(Constants.MovieDBConst.RESULTS_KEY);

        when(httpConnection.isNetworkConnected()).thenReturn(true);
        when(httpConnection.fetchContent(any(URL.class))).thenReturn(dummyJson);

        provider.getListOfMoviesFilteredBy(filter,1);
        provider.getListOfMoviesFilteredBy(filter,2);
        provider.getListOfMoviesFilteredBy(filter,3);

        int count = dataStore.count(filter);

        assertEquals(2,count);
    }

    @Test
    public void testDate() throws SyncException, JSONException
    {
        Date date = new Date(0L);
    }

    private static JSONObject dummyMovieData1()  throws JSONException
    {
        JSONObject movie1 = new JSONObject();

        movie1.put(Constants.MovieDBConst.TITLE_KEY,"Patch Adams");
        movie1.put(Constants.MovieDBConst.POSTER_PATH_KEY,"https://image.tmdb.org/t/p/original/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg");
        movie1.put(Constants.MovieDBConst.RELEASE_DATE_KEY,"2016-01-01");
        movie1.put(Constants.MovieDBConst.VOTE_AVERAGE_KEY,5.0);
        movie1.put(Constants.MovieDBConst.OVERVIEW_KEY,"Nice");
        movie1.put(Constants.MovieDBConst.BACKDROP_PATH_KEY,"https://image.tmdb.org/t/p/original/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg");

        JSONArray resultArray = new JSONArray();
        resultArray.put(movie1);

        JSONObject responseJSON = new JSONObject();
        responseJSON.put(Constants.MovieDBConst.RESULTS_KEY,resultArray);
        responseJSON.put(Constants.MovieDBConst.TOTAL_PAGES_KEY,Integer.MAX_VALUE);
        return responseJSON;
    }

    private static JSONObject dummyMovieData2()  throws JSONException
    {
        JSONObject movie1 = new JSONObject();

        movie1.put(Constants.MovieDBConst.TITLE_KEY,"Patch Adams");
        movie1.put(Constants.MovieDBConst.POSTER_PATH_KEY,"https://image.tmdb.org/t/p/original/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg");
        movie1.put(Constants.MovieDBConst.RELEASE_DATE_KEY,"2016-01-01");
        movie1.put(Constants.MovieDBConst.VOTE_AVERAGE_KEY,5.0);
        movie1.put(Constants.MovieDBConst.OVERVIEW_KEY,"Nice");
        movie1.put(Constants.MovieDBConst.BACKDROP_PATH_KEY,"https://image.tmdb.org/t/p/original/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg");

        JSONArray resultArray = new JSONArray();
        resultArray.put(movie1);

        JSONObject responseJSON = new JSONObject();
        responseJSON.put(Constants.MovieDBConst.RESULTS_KEY,resultArray);
        responseJSON.put(Constants.MovieDBConst.TOTAL_PAGES_KEY,2);
        return responseJSON;
    }
}

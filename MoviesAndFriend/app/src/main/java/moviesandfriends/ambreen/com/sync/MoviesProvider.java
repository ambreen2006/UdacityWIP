/*
 * Copyright (C) 2016 Ambreen Haleem (ambreen2006@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by ambreen on 11/4/16.
 */

package moviesandfriends.ambreen.com.sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.moviesandfriend.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implements movies provider interface by connecting to the MovieDB public API.
 * It handles the code to manage pagination, ignore multiple request for the same page and write
 * contents in the data store object.
 */
public class MoviesProvider implements IMoviesProvider {

    private        ClientHttpConnection mHttpConnection;
    private        LocalDataStore mDataStore;
    static private String sKey;

    /**
     * Currently used by test to inject mocked http connection object.
     * @param httpConnection
     */
    public MoviesProvider(ClientHttpConnection httpConnection) {
        mHttpConnection = httpConnection;
        mDataStore = DataStoreFactory.getMovieDataStore();
        sKey = BuildConfig.MOVIE_DB_KEY;
    }

    /**
     * Constructor that accepts the activity context. This context is then passed to the
     * ClientHttpConnection class for network connectivity status check
     * @param context is the activity context
     */
    public MoviesProvider(Context context)
    {
        this((new ClientHttpConnection(context)));
    }

    /**
     * Contains the steps required to get the content, parse it, and store it in the data store.
     * Movies provider uses the MovieList object to query whether data for the specific page is
     * already downloaded or not but that information is written once the page is retrieved and
     * parsed. It is therefore upto the user of the class to serialize the request.
     * @param filter category such as popular top_rated
     * @param page page number to request the movie DB API
     * @return Result enum specifying success, failure, completed (to indicate all pages are
     * downloaded) or ignored as a page request is already sent.
     * @throws SyncException
     */
    public Result getListOfMoviesFilteredBy(String filter, int page) throws SyncException
    {
        if(!mHttpConnection.isNetworkConnected()) {
            throw new SyncException("Network connectivity could not be established");
        }

        try {
            LocalDataStore.MovieList movieList = mDataStore.getMoviesList(filter);

            if(movieList.lastPage == page) {
                Log.d(Constants.LogTagConst.MOVIES_PROVIDER, "Ignoring page "+page+" for filter "+filter);
                return Result.DUPLICATED_IGNORED;
            }

            if (movieList.totalNumberOfPages != Integer.MAX_VALUE && page > movieList.totalNumberOfPages) {
                Log.d(Constants.LogTagConst.MOVIES_PROVIDER, "Ignoring page request " +
                        "because all pages are downloaded for the filter "+filter);
                return Result.COMPLETED;
            }

            Log.d(Constants.LogTagConst.MOVIES_PROVIDER, "Requesting page "+page+" for filter "+filter);

            JSONObject jsonObject = mHttpConnection.fetchContent(getURL(filter,page));

            if (jsonObject != null) {

                JSONArray results = jsonObject.getJSONArray(Constants.MovieDBConst.RESULTS_KEY);
                movieList.totalNumberOfPages = jsonObject.getInt(Constants.MovieDBConst.TOTAL_PAGES_KEY);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject aMovie = results.getJSONObject(i);

                    MovieData movieData = new MovieData();
                    movieData.title = aMovie.getString(Constants.MovieDBConst.TITLE_KEY);
                    movieData.posterPath = aMovie.getString(Constants.MovieDBConst.POSTER_PATH_KEY);
                    movieData.overview = aMovie.getString(Constants.MovieDBConst.OVERVIEW_KEY);
                    String dateString = aMovie.getString(Constants.MovieDBConst.RELEASE_DATE_KEY);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MovieDBConst.RELASE_DATE_FORMAT);
                    try {
                        movieData.releaseDate = dateFormat.parse(dateString);
                    } catch (ParseException parseEx) {
                        movieData.releaseDate = new Date(0L);
                        Log.e(Constants.LogTagConst.MOVIES_PROVIDER, parseEx.getMessage());
                    }

                    movieData.backdropPath = aMovie.getString(Constants.MovieDBConst.BACKDROP_PATH_KEY);

                    try {
                        movieData.voteAverage = (float) aMovie.getDouble(Constants.MovieDBConst.VOTE_AVERAGE_KEY);
                    } catch (NumberFormatException numFormatEx) {
                        movieData.voteAverage = Constants.MovieDBConst.INVALID_VOTES_AVG;
                        Log.e(Constants.LogTagConst.MOVIES_PROVIDER, numFormatEx.getMessage());
                    }

                    movieList.contents.add(movieData);
                }

                movieList.lastPage = page;
                mDataStore.setDataList(filter, movieList);
                return Result.SUCCESS;
            }
            else
            {
                return Result.FAILED;
            }

        }
        catch (JSONException | MalformedURLException exception) {
            Log.e(Constants.LogTagConst.MOVIES_PROVIDER,exception.toString());
            return Result.FAILED;
        }
    }

    /**
     * Create URL for the main movie data request.
     * @param filter string represents category
     * @param page int representing teh page to request from the moviesDB API.
     * @return URL object for the movieDB Movies API
     * @throws MalformedURLException
     */
    private URL getURL(String filter, int page) throws MalformedURLException
    {
        String stringURL = Constants.MovieDBConst.URL_PREFIX;
        stringURL += filter;
        stringURL += "?";
        stringURL += Constants.MovieDBConst.PAGE_REQUEST+"="+page;
        stringURL += "&"+Constants.MovieDBConst.API_KEY_REQUEST+"=";
        stringURL += sKey;
        return new URL(stringURL);
    }

    /**
     * Creates URL path for the poster or backdrop image
     * @param posterPath is the path portion returned as a result of the getListOfMoviesFilteredBy(String,int)
     *                   call.
     * @param size specifies one of the size options made available by the movie db api
     * @return string representing url to the poster or backdrop image
     */
    @NonNull
    public static String getStringURLForPoster(String posterPath, int size)
    {
        String imageURLStr = Constants.MovieDBConst.THUMBNAIL_URL_PREFIX + size + posterPath;
        return imageURLStr;
    }
}

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

import java.util.Hashtable;
import java.util.Vector;

/**
 * LocalDataStore implements the IDataStore Interface. It specifically provides the logic to
 * store movie data for this project. Data is referenced using filter categories such as
 * popular (movies) or top_rated. It also holds meta data related to the request itself. E.g.
 * How many pages are available to download for a category such as "popular" and which page is the
 * last one successfully fetched.
 */
public class LocalDataStore implements IDataStore {

    /**
     * MovieList is the class that holds the actual data. Every page requested returns an array
     * of movies along with the information about that query such as number of pages and the current
     * page. The movie data itself contains information about the movie such as it's title, release
     * date, etc. That movie data is populated in the MovieData object and stored in the contents
     * vector. This contents has no order except in which the data is received and populated.
     * totalNumberOfPages stores the max pages available returned by the API.
     * lastPage stores the count of the last page downloaded.
     */
    public class MovieList {
        public int lastPage;
        public int totalNumberOfPages;
        public Vector<MovieData> contents;

        MovieList() {
            contents = new Vector<>();
            lastPage = 0;
            totalNumberOfPages = Integer.MAX_VALUE;
        }
    }

    private Hashtable<String,MovieList>  mMoviesByFilter = new Hashtable<String,MovieList>();

    /**
     * Use this method to get the MovieData object associated with a filter (e.g "popular" or
     * "top_rated) and and index which is the index location in the contents vector associated with
     * the given filter.
     * @param filter string specifies the category such as "popular"
     * @param index int location in the vector for movie data
     * @return MovieData object
     */
    public MovieData getData(String filter, int index) {

        MovieList movieVector =  mMoviesByFilter.get(filter);
        if(movieVector != null) {
           return movieVector.contents.get(index);
        }
        return null;
    }

    /**
     * Sets the MovieList object in a hash table with the filter key. Hash table is thread safe
     * and it is safe to call this method concurrently.
     * @param filter string specifies the category such as "popular"
     * @param dataList is the MovieList object
     */
    public void setDataList(String filter, MovieList dataList) {
        mMoviesByFilter.put(filter,dataList);
        System.out.println(mMoviesByFilter.get(filter));
    }

    /**
     * Number of movies meta data currently downloaded and stored for a category
     * @param filter string specifies the category such as "popular"
     * @return count of movies for specific category
     */
    public int count(String filter) {

        if(mMoviesByFilter.containsKey(filter)) {
            return mMoviesByFilter.get(filter).contents.size();
        }
        else return 0;
    }

    /**
     * MovieList object associated with a filter. See MovieList definition for more info
     * @param filter string specifies the category such as "popular"
     * @return MovieList object
     */
    public MovieList getMoviesList(String filter) {

        if (mMoviesByFilter.containsKey(filter)) {
            return mMoviesByFilter.get(filter);
        }
        else {
            return new MovieList();
        }
    }

    /**
     * Removes all contents associated with the filter
     * @param filter string specifies the category such as "popular"
     */
    public void removeDataForFilter(String filter) {
        mMoviesByFilter.remove(filter);
    }
}

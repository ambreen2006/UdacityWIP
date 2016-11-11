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
 * Created by ambreen on 11/7/16.
 */

package moviesandfriends.ambreen.com.constants;

/** Holding constants for use across the app */
public final class Constants {

    public static final class LogTagConst {
        public final static String COMM = "COMM";
        public final static String MOVIES_PROVIDER = "MOVIES_PROVIDER";
        public final static String VIEW = "UI";
        public final static String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    }

    public static final class FilterTypeConst {
        public final static String TOP_RATED = "top_rated";
        public final static String POPULAR = "popular";
    }

    public static final class MovieDBConst {
        public final static String URL_PREFIX="https://api.themoviedb.org/3/movie/";
        public final static String THUMBNAIL_URL_PREFIX = "http://image.tmdb.org/t/p/w";
        public final static String BACKDROP_PATH_PREFIX = "/xBKGJQsAIeweesB79KC89FpBrVr.jpg";

        public final static String TITLE_KEY = "title";
        public final static String RELEASE_DATE_KEY = "release_date";
        public final static String POSTER_PATH_KEY = "poster_path";
        public final static String VOTE_AVERAGE_KEY = "vote_average";
        public final static String OVERVIEW_KEY = "overview";
        public final static String BACKDROP_PATH_KEY = "backdrop_path";

        public final static int    POSTER_SMALL = 154;
        public final static int    POSTER_LARGE = 1280;

        public final static String PAGE_REQUEST  = "page";
        public final static String API_KEY_REQUEST = "api_key";
        public final static String FILTER_REQUEST= "filter_request";
        public final static String RESULTS_KEY = "results";
        public final static String TOTAL_PAGES_KEY = "total_pages";

        public final static String RELASE_DATE_FORMAT = "yyyy-MM-DD";

        public final static float INVALID_VOTES_AVG = -1.0f;
    }

    public static final class BackgroundServiceConst {
        public static final String DEFAULT_NAME = "BackgroundService";
        public static final String RESULT_KEY = "service_result";
        public static final String BROADCAST_MSG = "moviesandfriend.ambree.com.background.broadcast";
    }

    public static final class MovieSelectionConst {
        public final static String SELECTED_INDEX = "selected_index";
        public final static String SELECTED_FILTER = "selected_filter";
    }

    public static final class TextFormattingConst {
        public static final String OVERVIEW_FORMAT_BEGIN = "<html><body><p align=justify><font color=white>";
        public static final String OVERVIEW_FORMAT_END = "</font></p></html></body>";
        public static final String HTML_TYPE = "text/html";
    }

    public static final class HttpCommConst {
        public static final String METHOD_GET = "GET";
        public static final int READ_TIMEOUT = 10000; /* milliseconds */
        public static final int CONNECT_TIMEOUT = 15000; /* milliseconds */
    }
}

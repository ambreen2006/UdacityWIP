package moviesandfriends.ambreen.com.constants;

/**
 * Created by ambreen on 11/7/16.
 */

public final class Constants
{
    public final class LogTag
    {
        public final static String COMM = "COMM";
        public final static String MOVIES_PROVIDER = "MOVIES_PROVIDER";
        public final static String VIEW = "UI";
        public final static String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    }

    public final class FilterType
    {
        public final static String TOP_RATED = "top_rated";
        public final static String POPULAR = "popular";
    }

    public final class MovieDB
    {
        public final static String URL_PREFIX="https://api.themoviedb.org/3/movie/";
        public final static String THUMBNAIL_URL_PREFIX = "http://image.tmdb.org/t/p/w";
        public final static String backdropPathPrefix = "/xBKGJQsAIeweesB79KC89FpBrVr.jpg";

        public final static String TITLE_KEY = "title";
        public final static String RELEASE_DATE_KEY = "release_date";
        public final static String POSTER_PATH_KEY = "poster_path";
        public final static String VOTE_AVERAGE_KEY = "vote_average";
        public final static String OVERVIEW_KEY = "overview";
        public final static String BACKDROP_PATH_KEY = "backdrop_path";

        public final static int    psoterSmall = 154;
        public final static int    posterLarge = 500;
    }
}

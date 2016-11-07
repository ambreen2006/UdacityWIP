package moviesandfriends.ambreen.com.sync;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by ambreen on 11/4/16.
 */

public class LocalDataStore
{
    public class MovieData
    {
        String posterPath;
        String title;
    }

    ArrayList<MovieData> moviesContent = new ArrayList<MovieData>();

    private static LocalDataStore ourInstance = new LocalDataStore();

    public static LocalDataStore getInstance() {
        return ourInstance;
    }

    private LocalDataStore() {

    }

    public String getTitle(int index)
    {
        if(index < moviesContent.size()) {
            return moviesContent.get(index).title;
        }
        else
            return  "";
    }

    public void addData(MovieData data)
    {
        moviesContent.add(data);
    }

    public String getPosterPath(int index)
    {
        return moviesContent.get(index).posterPath;
    }

    public int count()
    {
        return moviesContent.size();
    }

    public class MovieInformation
    {
        public String posterPath;
        public String title;
    }
}

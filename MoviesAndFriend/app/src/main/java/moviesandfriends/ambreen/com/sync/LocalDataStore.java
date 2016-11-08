package moviesandfriends.ambreen.com.sync;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by ambreen on 11/4/16.
 */

public class LocalDataStore<T> implements IDataStore<T>
{
    Hashtable<String,Vector<T>> contents = new Hashtable<String,Vector<T>>();

    public T getData(String filter, int index)
    {
        Vector<T> movieVector = contents.get(filter);
        if(movieVector != null)
        {
           return movieVector.get(index);
        }
        return null;
    }

    public void addData(String filter,T data)
    {
        Vector<T> movieVector;
        movieVector = contents.get(filter);
        if(movieVector == null)
        {
            movieVector = new Vector<T>();
        }

        movieVector.add(data);
        contents.put(filter,movieVector);
    }

    public int count(String filter)
    {
        Vector<T> movieVector = contents.get(filter);
        if(movieVector != null)
        {
            return movieVector.size();
        }

        return 0;
    }

    public void removeDataForFilter(String filter)
    {
        contents.remove(filter);
    }

}

package moviesandfriends.ambreen.com.sync;

/**
 * Created by ambreen on 11/7/16.
 */

public interface IDataStore<T>
{
    public T getData(String filter, int index);
    public void addData(String filter,T data);
    public int count(String filter);
}

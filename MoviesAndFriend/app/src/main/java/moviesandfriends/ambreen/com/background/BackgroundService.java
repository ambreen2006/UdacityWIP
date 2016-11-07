package moviesandfriends.ambreen.com.background;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

/**
 * Created by ambreen on 11/4/16.
 */

public class BackgroundService extends IntentService {

    public static final String BROADCAST_MSG="moviesandfriend.ambree.com.background.broadcast";
    public static final String STATUS_CODE="moviesandfriend.ambree.com.background.status";
    Intent broadcastIntent;

    public BackgroundService()
    {
        super("BackgroundService");
        broadcastIntent = new Intent(BROADCAST_MSG);
    }

    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        Log.d("BackgroundService","OnHandleIntent");
        MoviesProvider provider = new MoviesProvider(this);
        try
        {
            provider.getListOfMoviesFilteredByType(1);
            broadcastIntent.putExtra(STATUS_CODE, LocalDataStore.getInstance().count());
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

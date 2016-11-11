package moviesandfriends.ambreen.com.background;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.sync.DataStoreFactory;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

public class BackgroundService extends IntentService {

    Intent broadcastIntent;

    public BackgroundService()
    {
        //TODO: changed string that's passed to super to be dynamic
        super(Constants.BackgroundServiceConst.DEFAULT_NAME);
        broadcastIntent = new Intent(Constants.BackgroundServiceConst.BROADCAST_MSG);
    }

    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        MoviesProvider provider = new MoviesProvider(this);

        String filter = workIntent.getStringExtra(Constants.MovieDBConst.FILTER_REQUEST);
        int page = workIntent.getIntExtra(Constants.MovieDBConst.PAGE_REQUEST,0);
        try
        {
            int statusCode = (provider.getListOfMoviesFilteredBy(filter,page)).ordinal();

            broadcastIntent.putExtra(Constants.MovieDBConst.PAGE_REQUEST,page);
            broadcastIntent.putExtra(Constants.MovieDBConst.FILTER_REQUEST,filter);
            broadcastIntent.putExtra(Constants.BackgroundServiceConst.RESULT_KEY,statusCode);

            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
        catch(SyncException e)
        {
            Log.e(Constants.LogTagConst.BACKGROUND_SERVICE,e.toString());
        }
    }
}

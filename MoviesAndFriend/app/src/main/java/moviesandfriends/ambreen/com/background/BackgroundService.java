package moviesandfriends.ambreen.com.background;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.exceptions.SyncException;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

public class BackgroundService extends IntentService {

    private MoviesProvider mProvider;

    public BackgroundService()
    {
        //TODO: changed string that's passed to super to be dynamic
        super(Constants.BackgroundServiceConst.DEFAULT_NAME);
        mProvider = new MoviesProvider(this);
    }

    @Override
    protected void onHandleIntent(Intent workIntent)
    {
        String filter = workIntent.getStringExtra(Constants.MovieDBConst.FILTER_REQUEST);
        Intent broadcastIntent = new Intent(Constants.BackgroundServiceConst.BROADCAST_MSG);

        int statusCode = -1;
        String syncFailureErrorMsg = "";

        try {
            statusCode = (mProvider.getListOfMoviesFilteredBy(filter)).ordinal();
        } catch(SyncException e)
        {
            statusCode = MoviesProvider.Result.FAILED.ordinal();
            broadcastIntent.putExtra(Constants.BackgroundServiceConst.SYNC_ERROR,e.toString());
            Log.e(Constants.LogTagConst.BACKGROUND_SERVICE,e.toString());
        }

        broadcastIntent.putExtra(Constants.MovieDBConst.FILTER_REQUEST,filter);
        broadcastIntent.putExtra(Constants.BackgroundServiceConst.RESULT_KEY,statusCode);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}

package moviesandfriends.ambreen.com.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import moviesandfriends.ambreen.com.Constants.LogTag;
import moviesandfriends.ambreen.com.exceptions.SyncException;

/**
 * Created by ambreen on 11/4/16.
 */

public class MovieSourceHttpConnection {

    private Context context;

    public MovieSourceHttpConnection(Context context)
    {
        this.context = context;
    }

    public JSONObject fetchContent(URL url) throws SyncException {

        InputStream inputStream = null;
        HttpURLConnection conn = null;
        int responseCode = 0;

        try
        {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");

            conn.connect();
            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                inputStream = conn.getInputStream();
                String responseStr = IOUtils.toString(inputStream);
                Log.d(LogTag.COMM, responseStr);
                return (JSONObject) new JSONTokener(responseStr).nextValue();
            }
            else
            {
                InputStream errorStream = conn.getErrorStream();
                String errorMsg = IOUtils.toString(errorStream);
                SyncException syncException = new SyncException("ServerError: "+errorMsg);
                syncException.responseCode = responseCode;
                throw syncException;
            }
        }
        catch (JSONException j)
        {
            Log.e(LogTag.COMM, j.getMessage() + Log.getStackTraceString(j));
        }
        catch (IOException e)
        {
            Log.e(LogTag.COMM,e.getMessage() + Log.getStackTraceString(e));
        }
        finally
        {
            if(inputStream != null)
            {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public boolean isNetworkConnected()
    {
        if(this.context != null)
        {
            ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                return true;
            }

            return false;
        }
        else
            return false;
    }
}

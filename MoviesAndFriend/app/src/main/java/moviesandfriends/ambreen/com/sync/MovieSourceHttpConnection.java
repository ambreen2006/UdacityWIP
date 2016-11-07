package moviesandfriends.ambreen.com.sync;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ambreen on 11/4/16.
 */

public class MovieSourceHttpConnection {

    public JSONObject fetchContent(URL url) throws ProtocolException, MalformedURLException, IOException {
        InputStream inputStream = null;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        try {
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("COMM", "Response " + response);
            if (response == 200) {
                inputStream = conn.getInputStream();
                String movieData = IOUtils.toString(inputStream);
                Log.d("COMM", movieData);
                return (JSONObject) new JSONTokener(movieData).nextValue();
            }
        }
        catch (JSONException j)
        {
            Log.e("COMM","Json conversion from string failed "+j.toString());
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if(inputStream != null)
            {
                inputStream.close();
            }
        }

        return null;
    }

}

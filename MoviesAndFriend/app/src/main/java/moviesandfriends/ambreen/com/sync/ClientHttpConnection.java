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
 *  Created by ambreen on 11/4/16.
 */

package moviesandfriends.ambreen.com.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.exceptions.SyncException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Establishes connection with the server for http(s) communication
 */
public class ClientHttpConnection implements IClientHttpConnection {

    private Context mContext;

    /** Pass activity context when creating the object. Context is used for testing network connectivity */
    ClientHttpConnection(Context context) {
        this.mContext = context;
    }

    /**
     * This method creates and post a GET request to the URL provided
     * @param url is the API to which to connect and fetch data from
     * @return A json object that was returned from the server
     * @throws SyncException
     */
    public JSONObject fetchContent(URL url) throws SyncException {

        InputStream mInputStream = null;
        HttpURLConnection mConnection;
        int mResponseCode;

        try {
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setReadTimeout(Constants.HttpCommConst.READ_TIMEOUT);
            mConnection.setConnectTimeout(Constants.HttpCommConst.CONNECT_TIMEOUT);
            mConnection.setRequestMethod(Constants.HttpCommConst.METHOD_GET);
            mConnection.connect();

            mResponseCode = mConnection.getResponseCode();
            if (mResponseCode == HttpURLConnection.HTTP_OK)
            {
                mInputStream = mConnection.getInputStream();
                String responseStr = IOUtils.toString(mInputStream);
                return (JSONObject) new JSONTokener(responseStr).nextValue();
            }
            else
            {
                InputStream errorStream = mConnection.getErrorStream();
                String errorMsg = IOUtils.toString(errorStream);
                SyncException syncException = new SyncException("ServerError: "+errorMsg);
                syncException.responseCode = mResponseCode;
                throw syncException;
            }
        }
        catch (JSONException | IOException j) {
            Log.e(Constants.LogTagConst.COMM, j.getMessage() + Log.getStackTraceString(j));
        }
        finally {
            if(mInputStream != null) {
                try {
                    mInputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /** Checks if network connectivity can  be established */
    public boolean isNetworkConnected()
    {
        if(this.mContext != null)
        {
            ConnectivityManager connMgr = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        else
            return false;
    }
}

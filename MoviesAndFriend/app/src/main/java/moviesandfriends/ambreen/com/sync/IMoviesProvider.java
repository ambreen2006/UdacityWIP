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
 * Created by ambreen on 11/4/16.
 */

package moviesandfriends.ambreen.com.sync;

import moviesandfriends.ambreen.com.exceptions.SyncException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Interface for a class that encapsulates logic of obtaining and storing movie data.
 */
public interface IMoviesProvider
{
    public enum Result
    {
        SUCCESS, FAILED, COMPLETED, DUPLICATED_IGNORED
    }

    /** Implementation of this method should include accessing the movies content and storing
     * the data in an object that conforms to IDataStore interface */
    Result getListOfMoviesFilteredBy(String filter, int page) throws SyncException;
}

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
 * Created by ambreen on 11/7/16.
 */

package moviesandfriends.ambreen.com.sync;

/** Factory method that returns a singleton DataStore object to be used globally */

public final class DataStoreFactory
{
    public static LocalDataStore sLocalDataStore = new LocalDataStore();

    /** Returns a LocalDataStore object which stores movie data */
    public static LocalDataStore getMovieDataStore()
    {
        return sLocalDataStore;
    }
}

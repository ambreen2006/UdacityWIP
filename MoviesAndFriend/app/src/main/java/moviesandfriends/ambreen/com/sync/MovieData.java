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

import java.util.Date;

/**
 * MovieData class stores all values from movies results array. It's only purpose is to provide
 * a UDT for a movie.
 */
public class MovieData {
    public String title;
    public String posterPath;
    public Date releaseDate;
    public float voteAverage;
    public String overview;
    public String backdropPath;

    public MovieData() {
    }

    public MovieData(String title, String posterPath, Date releaseDate, float voteAverage, String overview, String backdropPath) {
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.backdropPath = backdropPath;
    }

}
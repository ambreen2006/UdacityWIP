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

package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.sync.DataStoreFactory;
import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MovieData;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

/**
 * DetailActivity retrieves the selected index and filter from the intent, then selects the data
 * from the common data store and display the detail information.
 */
public class DetailActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String filter = intent.getStringExtra(Constants.MovieSelectionConst.SELECTED_FILTER);
        int    index  = intent.getIntExtra(Constants.MovieSelectionConst.SELECTED_INDEX,-1);

        LocalDataStore dataStore = DataStoreFactory.getMovieDataStore();

        TextView  titleView = (TextView) findViewById(R.id.title_view);
        ImageView imageView = (ImageView) findViewById(R.id.poster_view);
        TextView  releaseDateView = (TextView) findViewById(R.id.release_date_view);
        TextView  averageVoteView = (TextView) findViewById(R.id.average_vote_view);
        WebView   movieOverviewWebView = (WebView) findViewById(R.id.movie_overview_web_view);

        movieOverviewWebView.setBackgroundColor(Color.TRANSPARENT);

        MovieData mData = dataStore.getData(filter,index);

        String posterStringURL = MoviesProvider.getStringURLForPoster(mData.backdropPath, Constants.MovieDBConst.POSTER_LARGE);
        Picasso.with(this).load(posterStringURL).into((imageView));

        titleView.setText(mData.title);
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
        dateFormat.format(mData.releaseDate);
        releaseDateView.setText(dateFormat.format(mData.releaseDate));
        averageVoteView.setText(String.valueOf(mData.voteAverage));

        String htmlContent = Constants.TextFormattingConst.OVERVIEW_FORMAT_BEGIN+mData.overview+Constants.TextFormattingConst.OVERVIEW_FORMAT_END;
        movieOverviewWebView.loadData(htmlContent,Constants.TextFormattingConst.HTML_TYPE,null);
    }
}

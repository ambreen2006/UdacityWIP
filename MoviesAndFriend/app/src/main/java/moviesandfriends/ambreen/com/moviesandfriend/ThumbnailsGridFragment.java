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
 * Created by ambreen on 11/6/16.
 */

package moviesandfriends.ambreen.com.moviesandfriend;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import moviesandfriends.ambreen.com.background.BackgroundService;
import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.sync.DataStoreFactory;
import moviesandfriends.ambreen.com.sync.IMoviesProvider;
import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MovieData;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

/**
 * Sets up the fragment view for movie listing. Registers relevant listeners. Fetches new pages when
 * user reaches a specific threshold of the page and responds to user clicks for detail view.
 */

public class ThumbnailsGridFragment extends Fragment {

    private Context mContext;
    private String mFilter;
    private GridAdapter mGridAdapter;
    private GridView mGridView;
    private Intent mContentFetchIntent;

    // Initialize background service, grid, view adapter and set up listeners

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFilter = Constants.FilterTypeConst.POPULAR;
        updateTitle();

        mContext = this.getActivity();

        View layoutView = inflater.inflate(R.layout.grid_view_layout, container, false);
        mGridView = (GridView) layoutView.findViewById(R.id.gridview);

        mContentFetchIntent = new Intent(mContext, BackgroundService.class);
        IntentFilter backgroundServiceBroadcastFilter = new IntentFilter(Constants.BackgroundServiceConst.BROADCAST_MSG);
        BackgroundBroadcastReceiver bGroundServiceReceiver = new BackgroundBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bGroundServiceReceiver, backgroundServiceBroadcastFilter);

        mGridAdapter = new GridAdapter(mContext);
        mGridView.setAdapter(mGridAdapter);
        setHasOptionsMenu(true);

        //region listeners

        /**
         * When user clicks on a grid view item. Open the new activity to display the details.
         */
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constants.MovieSelectionConst.SELECTED_INDEX, position);
                intent.putExtra(Constants.MovieSelectionConst.SELECTED_FILTER, mFilter);
                startActivity(intent);
            }
        });

        /**
         * Request new page if user scrolled to the end
         */
        mGridView.setOnScrollListener(new GridView.OnScrollListener() {

            int visibleThreshold = Constants.GridAdapterConst.VISIBLE_THRESHOLD_DEFAULT;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (visibleThreshold < mGridView.getNumColumns()) {

                    visibleThreshold = visibleItemCount / 2;
                    Log.d(Constants.LogTagConst.VIEW, "Updated visible threshold for scrolling: " + visibleItemCount);
                }

                if (firstVisibleItem + visibleItemCount + visibleThreshold >= totalItemCount) {
                    dispatchRequestForMovieData(mFilter);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }
        });

        //endregion
        return layoutView;
    }

    /**
     * Sets up the menu which is used to select filters such as popular and top_rated
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
    }

    /**
     * When user select a sort option filter, dispatch background request to fetch data
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ratings_menu:
                this.mFilter = Constants.FilterTypeConst.TOP_RATED;
                dispatchRequestForMovieData(this.mFilter);
                mGridAdapter.notifyDataSetChanged();
                this.updateTitle();
                mGridView.smoothScrollToPosition(0);
                return true;

            case R.id.popular_menu:
                this.mFilter = Constants.FilterTypeConst.POPULAR;
                this.updateTitle();
                dispatchRequestForMovieData(this.mFilter);
                mGridView.smoothScrollToPosition(0);
                return true;

            default:
                return false;

        }

    }

    /**
     * If decided to send the request for more data. Select the right page to request for the
     * selected filter category. Then dispatch the fetch request.
     *
     * @param rFilter filter category
     */
    private void dispatchRequestForMovieData(String rFilter) {
        mContentFetchIntent.putExtra(Constants.MovieDBConst.FILTER_REQUEST, rFilter);
        mContext.startService(mContentFetchIntent);
    }

    private void updateTitle() {
        String newTitle = getResources().getString(R.string.app_name) + ": " + mFilter;
        getActivity().setTitle(newTitle);
    }

    /**
     * Handles response back from background intent service.
     */
    class BackgroundBroadcastReceiver extends BroadcastReceiver {

        int failedOrdinal = MoviesProvider.Result.FAILED.ordinal();
        int successOrdinal = MoviesProvider.Result.SUCCESS.ordinal();

        @Override
        public void onReceive(Context context, Intent intent) {

            //TODO: Handle the case when Result.COMPLETE is returned. There is no more data to fetch and there no need to start background activity
            int result = intent.getIntExtra(Constants.BackgroundServiceConst.RESULT_KEY, failedOrdinal);
            if (successOrdinal == result) {
                mGridAdapter.notifyDataSetChanged();
            } else if (failedOrdinal == result) {
                String errorMsg = intent.getStringExtra(Constants.BackgroundServiceConst.SYNC_ERROR);
                if (errorMsg.length() > 0) {
                    Toast toast = Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    /**
     * Sets the poster image in the grid view.
     */
    public class GridAdapter extends BaseAdapter {

        private Context mContext;
        private LocalDataStore mDataStore;

        GridAdapter(Context c) {
            mContext = c;
            mDataStore = DataStoreFactory.getMovieDataStore();
        }

        public int getCount() {
            return mDataStore.count(mFilter);
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
            } else {
                imageView = (ImageView) convertView;
            }

            MovieData mData = mDataStore.getData(mFilter, position);

            String imageURL = MoviesProvider.getStringURLForPoster(mData.posterPath, Constants.MovieDBConst.POSTER_SMALL);
            Picasso.with(mContext).load(imageURL).into(imageView);
            return imageView;
        }
    }
}

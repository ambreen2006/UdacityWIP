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
import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MovieData;

/**
 * Created by ambreen on 11/6/16.
 */

public class ThumbnailsGridFragment extends Fragment {

    Context context;
    String  filter;
    LocalDataStore<MovieData> dataStore;
    GridAdapter gridAdapter;
    GridView    gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.filter = Constants.FilterType.POPULAR;
        this.context = this.getActivity();
        this.dataStore = DataStoreFactory.getMovieDataStore();

        View layoutView = inflater.inflate(R.layout.grid_view_layout, container, false);

        IntentFilter backgroundServiceBroadcastFilter = new IntentFilter(BackgroundService.BROADCAST_MSG);
        BackgroundBroadcastReceiver bgroundServiceReceiver = new BackgroundBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bgroundServiceReceiver, backgroundServiceBroadcastFilter);

        requestMovieData(Constants.FilterType.POPULAR);
        requestMovieData(Constants.FilterType.TOP_RATED);

        this.gridView = (GridView) layoutView.findViewById(R.id.gridview);
        this.gridView.setColumnWidth((int) getResources().getDimension(R.dimen.poster_thumbnail_width));

        this.gridAdapter = new GridAdapter(context);
        this.gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("SELECTED_INDEX",position);
                intent.putExtra("SELECTED_FILTER",filter);
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);

        final View footerView = layoutView.findViewById(R.id.load_more_view);
        gridView.setOnScrollListener(new GridView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount) {

                    // last item in grid is on the screen, show footer:
                    System.out.println("Showing load more button");
                    footerView.setVisibility(View.VISIBLE);

                } else if (footerView.getVisibility() != View.GONE) {

                    // last item in grid not on the screen, hide footer:
                    footerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }
        });

        return layoutView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.sort_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ratings_menu:
                this.filter = Constants.FilterType.TOP_RATED;
                requestMovieData(filter);
                this.gridAdapter.notifyDataSetChanged();
                this.gridView.smoothScrollToPosition(0);
                return true;

            case R.id.popular_menu:
                this.filter = Constants.FilterType.POPULAR;
                requestMovieData(filter);
                this.gridAdapter.notifyDataSetChanged();
                this.gridView.smoothScrollToPosition(0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestMovieData(String rFilter)
    {
        if(dataStore.count(rFilter) == 0)
        {
            Intent intent = new Intent(context, BackgroundService.class);
            intent.putExtra("filter", rFilter);
            context.startService(intent);
        }
    }

    class BackgroundBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(Constants.LogTag.VIEW,"broadcase received");
            gridAdapter.notifyDataSetChanged();
        }
    }

    public class GridAdapter extends BaseAdapter {

        private Context mContext;
        LocalDataStore dataStore;

        int thumbImgWidth = 0;
        int thumbImgHeight = 0;

        public GridAdapter(Context c)
        {
            mContext = c;
            dataStore = DataStoreFactory.getMovieDataStore();

            thumbImgWidth = (int) getResources().getDimension(R.dimen.poster_thumbnail_width);
            thumbImgHeight =(int) getResources().getDimension(R.dimen.poster_thumbnail_height);
        }

        public int getCount()
        {
            return dataStore.count(filter);
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null)
            {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(thumbImgWidth,thumbImgHeight));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            else
            {
                imageView = (ImageView) convertView;
            }

            StringBuilder imageURL = new StringBuilder();
            MovieData mData = (MovieData) dataStore.getData(filter,position);

            imageURL.append(Constants.MovieDB.THUMBNAIL_URL_PREFIX).append(Constants.MovieDB.psoterSmall).append(mData.posterPath);
            System.out.println(imageURL.toString());
            Picasso.with(mContext).load(imageURL.toString()).into((ImageView)(imageView));
            return imageView;
        }
    }
}

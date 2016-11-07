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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import moviesandfriends.ambreen.com.background.BackgroundService;
import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MovieData;

/**
 * Created by ambreen on 11/6/16.
 */

public class ThumbnailsGridFragment extends Fragment {

    Context context;
    GridCellAdapter cellAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Context context = this.getActivity();
        View layoutView = inflater.inflate(R.layout.grid_view_layout, container, false);
        Intent intent = new Intent(context, BackgroundService.class);
        context.startService(intent);

        IntentFilter backgroundServiceBroadcastFilter = new IntentFilter(BackgroundService.BROADCAST_MSG);

        BackgroundBroadcastReceiver bgroundServiceReceiver = new BackgroundBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bgroundServiceReceiver, backgroundServiceBroadcastFilter);

        GridView gridView = (GridView) layoutView.findViewById(R.id.gridview);

        gridView.setColumnWidth((int) getResources().getDimension(R.dimen.poster_thumbnail_width));

        cellAdapter = new GridCellAdapter(context);
        gridView.setAdapter(cellAdapter);

        setHasOptionsMenu(true);

        return layoutView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.sort_menu,menu);
    }

    class BackgroundBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            cellAdapter.notifyDataSetChanged();
        }
    }

    public class GridCellAdapter extends BaseAdapter {

        private Context mContext;
        LocalDataStore dataStore;

        int thumbImgWidth = 0;
        int thumbImgHeight = 0;
        private static final int thumbRequestWidth = 185;

        public GridCellAdapter(Context c)
        {
            mContext = c;
            dataStore = LocalDataStore.getInstance();

            thumbImgWidth = (int) getResources().getDimension(R.dimen.poster_thumbnail_width);
            thumbImgHeight =(int) getResources().getDimension(R.dimen.poster_thumbnail_height);
        }

        public int getCount() {
            Log.d("Activity", "Total Count " + dataStore.count("popular"));
            return dataStore.count("popular");
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
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

            MovieData mData = (MovieData) dataStore.getData("popular",position);

            imageURL.append("http://image.tmdb.org/t/p/w").append(thumbRequestWidth).append(mData.posterPath);
            Picasso.with(mContext).load(imageURL.toString()).into((ImageView)(imageView));
            return imageView;
        }
    }

}

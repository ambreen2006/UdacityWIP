package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.sync.DataStoreFactory;
import moviesandfriends.ambreen.com.sync.LocalDataStore;
import moviesandfriends.ambreen.com.sync.MovieData;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

public class DetailActivity extends AppCompatActivity {

    LocalDataStore<MovieData> dataStore;
    MoviesProvider            moviesProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String filter = intent.getStringExtra("SELECTED_FILTER");
        int    index  = intent.getIntExtra("SELECTED_INDEX",-1);

        this.dataStore = DataStoreFactory.getMovieDataStore();
        this.moviesProvider = new MoviesProvider(this);

        TextView  titleView = (TextView) findViewById(R.id.title_view);
        ImageView imageView = (ImageView) findViewById(R.id.poster_view);
        TextView  releaseDateView = (TextView) findViewById(R.id.release_date_view);
        TextView  averageVoteView = (TextView) findViewById(R.id.average_vote_view);
        TextView  movieOverviewView = (TextView) findViewById(R.id.movie_overview_view);

        MovieData mData = (MovieData) dataStore.getData(filter,index);

        String posterStringURL = this.moviesProvider.getStringURLForPoster(mData.posterPath, Constants.MovieDB.posterLarge);
        Picasso.with(this).load(posterStringURL.toString()).into((ImageView)(imageView));

        System.out.println(posterStringURL);

        titleView.setText(mData.title);
        releaseDateView.setText(mData.releaseDate);
        averageVoteView.setText(mData.voteAverage);
        movieOverviewView.setText(mData.overview);

    }
}

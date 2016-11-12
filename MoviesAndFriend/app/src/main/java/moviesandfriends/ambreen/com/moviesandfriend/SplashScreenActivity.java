package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import moviesandfriends.ambreen.com.background.BackgroundService;
import moviesandfriends.ambreen.com.constants.Constants;

/** Dispatches one page fetch request for each category and then starts the main Activity */

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Intent contentFetchIntent = new Intent(this, BackgroundService.class);
        contentFetchIntent.putExtra(Constants.MovieDBConst.FILTER_REQUEST,Constants.FilterTypeConst.POPULAR);
        this.startService(contentFetchIntent);

        contentFetchIntent.putExtra(Constants.MovieDBConst.FILTER_REQUEST,Constants.FilterTypeConst.TOP_RATED);
        this.startService(contentFetchIntent);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DELAY);
    }
}
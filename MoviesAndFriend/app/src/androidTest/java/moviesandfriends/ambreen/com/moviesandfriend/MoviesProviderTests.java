package moviesandfriends.ambreen.com.moviesandfriend;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import moviesandfriends.ambreen.com.constants.Constants;
import moviesandfriends.ambreen.com.sync.MoviesProvider;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MoviesProviderTests {

    private Context appContext;

    @Before
    public void Setup() {
        this.appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        assertEquals("moviesandfriends.ambreen.com.moviesandfriend", appContext.getPackageName());
    }

    @Test
    public void testPopularMovieContents() {
        MoviesProvider provider = new MoviesProvider(appContext);
        try
        {
            provider.getListOfMoviesFilteredBy(Constants.FilterTypeConst.POPULAR);
        }
        catch (Exception e) {
            Assert.assertNull(e);
        }
    }
}

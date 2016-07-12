package moviepart2.project.udacity.com.movieinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MovieListMainActivity extends AppCompatActivity implements MoviesAdapter.Callback{

    private final String LOG_TAG = MovieListMainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(),
                                MovieDetailFragment.DETAIL_MOVIE)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_movie_list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, moviepart2.project.udacity.com.movieinfo.SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String sort=preferences.getString(ApplicationConstants.SORT_KEY,"0");
        if(toolbar!=null) {
            switch (sort) {
                case "0":
                    toolbar.setTitle(getResources().getStringArray(R.array.sort_order_options)[0]);
                    break;
                case "1":
                    toolbar.setTitle(getResources().getStringArray(R.array.sort_order_options)[1]);
                    break;
            }
        }
    }


    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {

            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", movie);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MovieDetailFragment.DETAIL_MOVIE)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailMainActivity.class)
                    .putExtra("movie", (Parcelable) movie);
            startActivity(intent);
        }

    }

}

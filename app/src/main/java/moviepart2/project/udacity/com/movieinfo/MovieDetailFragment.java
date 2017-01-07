package moviepart2.project.udacity.com.movieinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ravi on 21/6/16.
 */
public class MovieDetailFragment extends Fragment {

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private Context context;
    private Movie movie;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar mToolbar;
    private ImageView mLandPosterView;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mRatingTextView;
    private RatingBar mRatingBar;
    private TextView mGenreTextView;
    private TextView mDateTextView;
    private TextView mVotesTextView;
    private TextView mDescTextView;
    private FloatingActionButton mFavouriteButton;
    private RecyclerView mTrailersView;
    private ListView mReviewsView;
    private CardView mReviewsCardview;
    private CardView mTrailersCardview;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;


    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movie != null) {
            new FetchTrailersTask().execute(movie.getIntegerId() + "");
            new FetchReviewsTask().execute(movie.getIntegerId() + "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable("movie");
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if (movie == null) {
            movie = MovieList.get(getContext()).getMovies().get(0);
            if (movie == null)
                return rootView;
        }

        mLandPosterView = ( ImageView ) rootView.findViewById(R.id.landPosterImageView);
        mPosterImageView = ( ImageView ) rootView.findViewById(R.id.posterImageView);
        mTitleTextView = ( TextView ) rootView.findViewById(R.id.titleTextView);
        mRatingTextView = ( TextView ) rootView.findViewById(R.id.ratingTextView);
        mRatingBar = ( RatingBar ) rootView.findViewById(R.id.ratingBar);
        mGenreTextView = ( TextView ) rootView.findViewById(R.id.genreTextView);
        mDateTextView = ( TextView ) rootView.findViewById(R.id.dateTextView);
        mVotesTextView = ( TextView ) rootView.findViewById(R.id.votesTextView);
        mDescTextView = ( TextView ) rootView.findViewById(R.id.descTextView);
        mFavouriteButton = ( FloatingActionButton ) rootView.findViewById(R.id.favButton);

        Picasso.with(getActivity()).load(movie.getBackdoorPosterUrl()).into(mLandPosterView);

        Picasso.with(getActivity()).load(movie.getPosterUrl()).into(mPosterImageView);

        String title = movie.getTitle();
        Spannable span = new SpannableString(title);
        int index = title.indexOf(':') + 1;
        if (index == 0) {
            index = title.length();
        }
        span.setSpan(new RelativeSizeSpan(1.25f), 0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleTextView.setText(span);

        double rating = movie.getRating();

        float stars = ( float ) (Math.round(rating * 10) / 20.0);
        mRatingBar.setRating(stars);

        rating = (( int ) (rating * 10)) / 10.0;
        String ratingText = rating + "/10";
        span = new SpannableString(ratingText);
        span.setSpan(new RelativeSizeSpan(1.8f), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mRatingTextView.setText(span);

        String genreText = "";
        int len = Math.min(movie.getGenreIds().size(), 3);
        for (int i = 0; i < len; ++i) {
            String tmp = movie.getGenreIds().get(i);
            tmp = ApplicationConstants.getGenre(tmp);
            if (tmp != null) {
                genreText = genreText + tmp;
                if (i != len - 1) {
                    genreText = genreText + ",\n";
                }
            }
        }
        mGenreTextView.setText(genreText);

        mDateTextView.setText(movie.getReleaseDate());

        mVotesTextView.setText(movie.getVoteCount() + " Votes");

        span = new SpannableString(movie.getOverview());
        span.setSpan(new RelativeSizeSpan(1.5f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mDescTextView.setText(span);

        if (MovieList.get(getContext()).IsFavority(movie)) {
            movie.setFavourite(true);
        }
        setUpFAB();
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFav = MovieList.get(getContext()).IsFavority(movie);
                int resId;
                if (!isFav) {
                    resId = R.string.favorite;
                    MovieList.get(getActivity()).addIntoFavouriteMovies(movie);
                    movie.setFavourite(true);
                } else {
                    resId = R.string.unfavorite;
                    MovieList.get(getActivity()).removeFromFavouriteMovies(movie);
                    movie.setFavourite(false);
                }
                Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
                movie.setFavourite(movie.isFavourite());
                setUpFAB();
            }
        });

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mCollapsingToolbar = ( CollapsingToolbarLayout ) rootView.findViewById(R.id.collapsing_toolbar);
            mCollapsingToolbar.setTitle(movie.getSimpleTitle());
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

            mToolbar = ( Toolbar ) rootView.findViewById(R.id.toolBar);
            AppCompatActivity activity = ( AppCompatActivity ) getActivity();
            activity.setSupportActionBar(mToolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
//            mToolbar.setNavigationIcon(android.R.drawable.ic_action_back);
        }

        mReviewsView = ( ListView ) rootView.findViewById(R.id.detail_reviews);
        mReviewsCardview = ( CardView ) rootView.findViewById(R.id.detail_reviews_cardview);
        mReviewAdapter = new ReviewAdapter(getContext(), new ArrayList<Review>());
        mReviewsView.setAdapter(new ReviewAdapter(getContext(), new ArrayList<Review>()));
        mReviewsView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view
                mReviewsView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mTrailersCardview = ( CardView ) rootView.findViewById(R.id.detail_trailers_cardview);
        mTrailersView = ( RecyclerView ) rootView.findViewById(R.id.detail_trailers);
        mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailers);
        mTrailersView.setAdapter(mTrailerAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTrailersView.setLayoutManager(llm);
        mTrailersView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view
                mTrailersView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        /*
        final View  temp_v = (HorizontalScrollView)rootView.findViewById(R.id.horizontalScrollView);

        temp_v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                temp_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        */
        return rootView;
    }

    private void setUpFAB() {
        boolean isFav = movie.isFavourite();
        if (isFav) {
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_red);
        } else {
            mFavouriteButton.setImageResource(R.drawable.ic_favorite_white);
        }
    }

    //Trailer fetch background task

    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private ArrayList<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");
            ArrayList<Trailer> results = new ArrayList<>();
            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                }
            }
            return results;
        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, ApplicationConstants.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = ( HttpURLConnection ) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    mTrailersCardview.setVisibility(View.VISIBLE);
                    if(mTrailerAdapter == null) {
                        mTrailerAdapter = new TrailerAdapter(getContext(), trailers);
                        mTrailersView.setAdapter(mTrailerAdapter);
                    }
                    else
                    {
                        mTrailerAdapter.setTrailers(trailers);
                        mTrailerAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private ArrayList<Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            ArrayList<Review> results = new ArrayList<>();

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                results.add(new Review(review));
            }
            return results;
        }

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, ApplicationConstants.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = ( HttpURLConnection ) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            if (reviews != null) {
                if (reviews.size() > 0) {
                    mReviewsCardview.setVisibility(View.VISIBLE);
                    mReviewAdapter = new ReviewAdapter(getContext(), reviews);
                    mReviewsView.setAdapter(mReviewAdapter);
                }
            }
        }
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        void onItemSelected(Movie movie);
    }

}
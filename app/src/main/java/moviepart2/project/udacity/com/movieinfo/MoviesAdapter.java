package moviepart2.project.udacity.com.movieinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravidwivedi on 28-04-2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener loadMoreListener;
    private Boolean showLoadingBar = true;

    public MoviesAdapter(Context context, ArrayList<Movie> movies, OnLoadMoreListener loadMoreListene) {
        this.context = context;
        this.movies = movies;
        this.loadMoreListener = loadMoreListene;
    }

    public void setShowLoadingBar(Boolean showLoadingBar) {
        this.showLoadingBar = showLoadingBar;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_layout, parent, false);
            return new MoviesViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING && showLoadingBar) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_list, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position > (movies.size() - 1) && showLoadingBar) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            loadMoreListener.onLoadMore();
        } else {
            MoviesViewHolder moviesViewHolder = (MoviesViewHolder) holder;
            moviesViewHolder.title.setText(movies.get(position).getTitle());
            Picasso.with(context).load(movies.get(position).getPosterUrl()).into(moviesViewHolder.banner);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size() + 1;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        public ImageView banner;
        public AppCompatTextView title;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            banner = (ImageView) itemView.findViewById(R.id.imageView);
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movies.get(getPosition());
                    ((Callback) context).onItemSelected(movie);
                }
            });
            title = (AppCompatTextView) itemView.findViewById(R.id.title);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
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

    @Override
    public int getItemViewType(int position) {
        return (movies.size() - 1) < position ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}

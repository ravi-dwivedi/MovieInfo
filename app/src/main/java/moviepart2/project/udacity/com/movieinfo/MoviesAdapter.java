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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravidwivedi on 28-04-2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {   View V= LayoutInflater.from(context).inflate(R.layout.movie_grid_layout,parent,false);
        return new MoviesViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position)
    {   holder.title.setText(movies.get(position).getTitle());
        Picasso.with(context).load(movies.get(position).getPosterUrl()).into(holder.banner);
    }
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder
    {   public ImageView banner;
        public AppCompatTextView title;
        public MoviesViewHolder(View itemView)
        {   super(itemView);
            banner=(ImageView) itemView.findViewById(R.id.imageView);

            /*
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movies.get(getPosition());
                    Intent startDetail = new Intent(context, MovieDetailMainActivity.class);
                    startDetail.putExtra(ApplicationConstants.KEY_MOVIE, movie.getId());
                    context.startActivity(startDetail);
                }
            });
            */


            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movies.get(getPosition());
                    ((Callback) context).onItemSelected(movie);
                }
            });
            title=(AppCompatTextView)itemView.findViewById(R.id.title);
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

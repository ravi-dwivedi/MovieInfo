package moviepart2.project.udacity.com.movieinfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

/**
 * Created by ravi on 21/6/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieTrailerHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        mContext = context;
        this.trailers = trailers;
    }

    @Override
    public int getItemCount() {
        if (trailers == null)
            return 0;
        else
            return trailers.size();
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public MovieTrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie_trailer, parent, false);
        return new MovieTrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieTrailerHolder holder, int position) {
        MovieTrailerHolder viewHolder = ( MovieTrailerHolder ) holder;
        String yt_thumbnail_url = "http://img.youtube.com/vi/" + trailers.get(position).getKey() + "/0.jpg";
        Picasso.with(this.mContext).load(yt_thumbnail_url).into(viewHolder.imageView);
        viewHolder.nameView.setText(trailers.get(position).getName());
    }

    public class MovieTrailerHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameView;

        public MovieTrailerHolder(View itemView) {
            super(itemView);
            imageView = ( ImageView ) itemView.findViewById(R.id.trailer_image);
            nameView = ( TextView ) itemView.findViewById(R.id.trailer_name);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View parentRow = ( View ) v.getParent();
                    RecyclerView recyclerView = ( RecyclerView ) parentRow.getParent();
                    int position = recyclerView.getChildAdapterPosition(parentRow);
                    Trailer trailer = trailers.get(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

}

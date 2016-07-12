package moviepart2.project.udacity.com.movieinfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ravidwivedi on 25-04-2016.
 */
public class Movie implements Serializable, Comparable<Movie>,Parcelable {

    public static final String POSTER_BASE_URL =
            "http://image.tmdb.org/t/p";
    public static final String BACKDOOR_BASE_URL =
            "http://image.tmdb.org/t/p";
    private String backdoorSize = "w342";
    private String posterSize = "w500";
    private UUID mId;
    private String mTitle;
    private String posterPath;
    private boolean isAdult;
    private String overview;
    private String releaseDate;
    private List<String> genreIds;
    private String backdoorPath;
    private double popularity;
    private int voteCount;
    private double rating;
    private int sortBy;
    private boolean isFavourite;
    private int id;

    public Movie() {
        mId = UUID.randomUUID();
        sortBy = 1;
        isFavourite = false;
    }

    @Override
    public int compareTo(Movie rhs) {
        if(sortBy == 1) {   // Sort By Popularity
            if(this.popularity > rhs.popularity) {
                return -1;
            } else if(this.popularity < rhs.popularity) {
                return 1;
            }
        } else if(sortBy == 2) {    // Sort by Ratings
            if(this.rating > rhs.rating) {
                return -1;
            } else if(this.rating < rhs.rating) {
                return 1;
            }
        }
        return 0;
    }

    public String getSimpleTitle() {
        return mTitle;
    }

    public String getTitle() {
        String title = mTitle;
        if(mTitle.indexOf(':') != -1) {
            title = mTitle.replace(":", ":\n");
        } else if(mTitle.length() > 21) {
            int numSpaces = 0;
            for(int i = 0; i < title.length(); ++i) {
                if(title.charAt(i) == ' ')
                    numSpaces++;
            }

            numSpaces /= 2;
            for(int i = 0; i < title.length(); ++i) {

                if(title.charAt(i) == ' ') {
                    if(numSpaces == 0) {
                        title = title.substring(0, i) + '\n' + title.substring(i+1);
                        break;
                    } else {
                        numSpaces--;
                    }

                }
            }
        }

        return title;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPosterUrl() {
        String posterUrl = POSTER_BASE_URL + "/" +posterSize + "" + posterPath;
        return posterUrl;
    }

    public int getIntegerId()
    {
        return  this.id;
    }
    public void setIntegerId(int id)
    {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdoorPosterUrl() {
        String posterUrl = BACKDOOR_BASE_URL + "/" + backdoorSize + "" + backdoorPath;
        return posterUrl;
    }

    public void setBackdoorPath(String backdoorPath) {
        this.backdoorPath = backdoorPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setSortBy(int num) {
        sortBy = num;
    }

    public boolean isFavourite() {

        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(backdoorSize);
        parcel.writeString(posterSize);
        parcel.writeString(mId.toString());
        parcel.writeString(mTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(backdoorPath);
        parcel.writeInt(voteCount);
        parcel.writeInt(sortBy);
        parcel.writeDouble(rating);
        parcel.writeDouble(popularity);
        parcel.writeStringList(genreIds);
        parcel.writeByte((byte) (isAdult ? 1 : 0));
        parcel.writeByte((byte) (isFavourite ? 1 : 0));
        parcel.writeInt(id);
    }


    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    private Movie(Parcel in) {
        backdoorSize = in.readString();
        posterSize = in.readString();
        mId = UUID.fromString(in.readString());
        mTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        backdoorPath = in.readString();
        voteCount = in.readInt();
        sortBy = in.readInt();
        rating = in.readDouble();
        popularity = in.readDouble();
        genreIds = new ArrayList<String>();
        in.readStringList(genreIds);
        isAdult = in.readByte() != 0;
        isFavourite =  in.readByte() != 0;
        id = in.readInt();

    }

}

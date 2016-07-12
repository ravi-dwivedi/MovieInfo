package moviepart2.project.udacity.com.movieinfo;

/**
 * Created by ravidwivedi on 28-04-2016.
 */

public final class ApplicationConstants {
    public static final String SORT_KEY="sort_order";
    public static final String API_URL ="https://api.themoviedb.org/3/discover/movie";
    public static final String API_KEY = "api key will come here";
    public static final int NUM_PAGES_FETCH = 3;
    public static final String KEY_MOVIE = "Movie";
    private static int[] mIndex = new int[] {
            28,
            12,
            16,
            35,
            80,
            99,
            18,
            10751,
            14,
            10769,
            36,
            27,
            10402,
            9648,
            10749,
            878,
            10770,
            53,
            10752,
            37
    };

    private static String[] mGenres = new String[] {
            "Action",
            "Adventure",
            "Animation",
            "Comedy",
            "Crime",
            "Documentary",
            "Drama",
            "Family",
            "Fantasy",
            "Foreign",
            "History",
            "Horror",
            "Music",
            "Mystery",
            "Romance",
            "Sci-Fi",
            "TV Movie",
            "Thriller",
            "War",
            "Western"
    };
    public static String getGenre(String in) {
        int index = Integer.parseInt(in);
        for(int i=0; i < mIndex.length; ++i) {
            if(index == mIndex[i]) {
                return mGenres[i];
            }
        }
        return null;
    }
}

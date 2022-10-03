package springApplication.IEMDBClasses;


import java.util.ArrayList;
import java.util.HashMap;
public class Movie {
    private int id;
    private String name;
    private String summary;
    private String releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Integer> cast;
    private double imdbRate;
    private int duration;
    private int ageLimit;
    private String image;
    private String coverImage;
    private ArrayList<String> cast_names;
    private double recommendationScore;

    private HashMap<String, Integer> ratings;
    private ArrayList<Comment> comments;

    public Movie() {
        ratings = new HashMap<>();
        comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getImage () {return image;}

    public String getCoverImage() {return coverImage;}

    public String getReleaseDate() {
        return releaseDate;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public ArrayList<Integer> getCast() {
        return cast;
    }

    public ArrayList<String> getCastNames() {
        return cast_names;
    }

    public void setCastNames(ArrayList<String> _cast_names) {
        this.cast_names = _cast_names;
    }

    public int getDuration() {
        return duration;
    }

    public double getImdbRate() {
        return imdbRate;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public HashMap<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Integer> _ratings) {
        ratings = _ratings;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> _comments) {
        comments = _comments;
    }

    public int getRatingsCount() {
        int rating_count = 0;
        int i = 0;
        while (i < ratings.size()) {
            rating_count += 1;
            i += 1;
        }
        return rating_count;
    }

    public double getRatingsAverage() {
        int rating_count = 0;
        double total_rating = 0;
        for (HashMap.Entry<String, Integer> rating_rec : ratings.entrySet()) {
            rating_count += 1;
            total_rating += rating_rec.getValue();
        }
        return total_rating / rating_count;
    }

    public void rate(String user_email, int score){
        ratings.put(user_email, score);
    }

    public String getName(){
        return name;
    }

    public String getDirector(){
        return director;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public int getNumberOfSameGenre(Movie movie) {
        int numbers = 0;
        ArrayList<String> m1_genres = movie.getGenres();
        for(String genre : genres){
            if(m1_genres.contains(genre))
                numbers += 1;
        }
        return numbers;
    }

    public int getGenreSimilarity(HashMap<Integer,Movie> watchlist){
        int similarity = 0;
        for (HashMap.Entry<Integer, Movie> watchlist_movie : watchlist.entrySet()) {
            similarity += getNumberOfSameGenre(watchlist_movie.getValue());
        }
        return similarity;
    }

    public void setRecommendationScore(HashMap<Integer,Movie> watchlist){
        recommendationScore = 3 * getGenreSimilarity(watchlist) + imdbRate + getRatingsAverage();
    }

    public double getRecommendationScore() {
        return recommendationScore;
    }
}

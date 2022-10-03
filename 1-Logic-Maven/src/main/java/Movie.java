import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
    private int id;
    private String name;
    private String summary;
    private String release_date;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Integer> cast;
    private double imdb_rate;
    private int duration;
    private int age_limit;

    private HashMap<String, Integer> ratings;
    private ArrayList<Comment> comments;

    public Movie(int _id, String _name, String _summary, String _release_date, String _director,
                 ArrayList<String> _writers, ArrayList<String> _genres, ArrayList<Integer> _cast,
                 double _imdb_rate, int _duration, int _age_limit) {
        this.id = _id;
        this.name = _name;
        this.summary = _summary;
        this.release_date = _release_date;
        this.director = _director;
        this.writers = _writers;
        this.genres = _genres;
        this.cast = _cast;
        this.imdb_rate = _imdb_rate;
        this.duration = _duration;
        this.age_limit = _age_limit;
        ratings = new HashMap<>();
        comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public ArrayList<String> getWriters() {
        return writers;
    }

    public ArrayList<Integer> getCast() {
        return cast;
    }

    public int getDuration() {
        return duration;
    }

    public int getAgeLimit() {
        return age_limit;
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
        for (HashMap.Entry<String, Integer> ignored : ratings.entrySet()) {
            rating_count += 1;
        }
        return rating_count;
    }

    public double getRatingsAverage() throws ArithmeticException{
        int rating_count = 0;
        double total_rating = 0;
        for (HashMap.Entry<String, Integer> rating_rec : ratings.entrySet()) {
            rating_count += 1;
            total_rating += rating_rec.getValue();
        }
        if (rating_count == 0)
            throw new ArithmeticException();

        return total_rating / rating_count;
    }

    public void rate(String user_email, int score) throws InvalidRateScoreError {
        if (score < 1 || score > 10)
            throw new InvalidRateScoreError();
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

}

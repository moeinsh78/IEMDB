package springApplication.IEMDBClasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class IEMDBSystem {

    private static IEMDBSystem single_instance = null;
    private User logged_in_user;

    public static IEMDBSystem getInstance() {
        if (single_instance == null) {
            single_instance = new IEMDBSystem();
            try {
                single_instance.setDB(
                        "http://138.197.181.131:5000/api/v2/movies",
                        "http://138.197.181.131:5000/api/v2/actors",
                        "http://138.197.181.131:5000/api/users"
                        );
            } catch(Exception ignored) {

            }
        }
        return single_instance;
    }

    public User getLoggedInUser() {
        return logged_in_user;
    }

    public boolean isLogin() {
        if(getLoggedInUser() == null)
            return false;
        else
            return true;
    }

    public void login(String user_email, String password) {
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_email);
        if(password.equals(user.getPassword()))
            this.logged_in_user = user;
        else
            return;
    }

    public void logout(){
        this.logged_in_user = null;
    }


    public String convertListOfStringsToString(ArrayList<String> list_of_items) {

        StringBuilder items_str = new StringBuilder();
        for (String item: list_of_items)
            items_str.append(item).append(", ");

        if(items_str.length() > 0)
            items_str = new StringBuilder(items_str.substring(0, items_str.length() - 2));

        return items_str.toString();
    }

    public void setDB(String movies_address, String actors_address, String users_address) {
        try {
            String movies_endpoints = getEndPoints(movies_address);
            importMovies(movies_endpoints);
            String actors_endpoints = getEndPoints(actors_address);
            importActors(actors_endpoints);
            String users_endpoints = getEndPoints(users_address);
            importUsers(users_endpoints);
        } catch (Exception ignored) {}
    }


    private String getEndPoints(String url){
        CloseableHttpResponse httpResponse;
        String endpoints;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getEntity() != null) {
                endpoints = EntityUtils.toString(httpResponse.getEntity());
            } else {
                endpoints = null;
            }
        } catch (IOException err) {
            endpoints = err.getMessage();
        }
        return endpoints;
    }

    public void importUsers(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<User> userList = mapper.readValue(endpoints, new TypeReference<>() {
        });
        Database db = Database.getInstance();
        for (User user : userList) {
            db.appendUser(user);
        }
    }

    public void importMovies(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<Movie> movieList = mapper.readValue(endpoints, new TypeReference<>() {});
        Database db = Database.getInstance();
        for (Movie movie : movieList) {
            db.appendMovie(movie);
        }
    }

    public void importActors(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<Actor> actorList = mapper.readValue(endpoints, new TypeReference<>() {
        });
        Database db = Database.getInstance();
        for (Actor actor : actorList) {
            db.appendActor(actor);
        }
        setActorsNamesForMovies();
    }


    public HashMap<Integer, Movie> getMoviesList() {
        Database db = Database.getInstance();
        return db.getMovies();
    }

    public Movie findMovieById(int movie_id) {
        Database db = Database.getInstance();
        return db.getMovieById(movie_id);
    }


    private void setActorsNamesForMovies(){
        Database db = Database.getInstance();
        for (HashMap.Entry<Integer, Movie> movie_rec : db.getMovies().entrySet()) {
            Movie movie = movie_rec.getValue();
            ArrayList<String> cast_names = new ArrayList<>();
            for (Integer actor_id : movie.getCast())
                cast_names.add(db.getActorsById(actor_id).getName());

            movie.setCastNames(cast_names);
        }
    }

    public HashMap<Integer, Movie> getMoviesByName(String name){
        HashMap<Integer, Movie> movies_by_name = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if(movie.getValue().getName().equals(name) || movie.getValue().getName().contains(name))
                movies_by_name.put(movie.getValue().getId(), movie.getValue());
        }
        return movies_by_name;
    }

    public HashMap<Integer, Movie> getMoviesByReleaseDate(String releaseDate){
        HashMap<Integer, Movie> movies_by_name = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if(movie.getValue().getReleaseDate().split("/")[0].equals(releaseDate))
                movies_by_name.put(movie.getValue().getId(), movie.getValue());
        }
        return movies_by_name;
    }

    public HashMap<Integer, Movie> getMoviesByGenre(String genre){
        HashMap<Integer, Movie> movies_by_name = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if(movie.getValue().getGenres().contains(genre))
                movies_by_name.put(movie.getValue().getId(), movie.getValue());
        }
        return movies_by_name;
    }

    public void addToWatchList(String user_email, Integer movie_id){
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_email);
        Movie movie = db.getMovieById(movie_id);
        user.addMovieToWatchList(movie);
    }

    public Actor getActorById(int id) {
        Database db = Database.getInstance();
        return db.getActorsById(id);
    }

    public HashMap<Integer, Movie> getActorMovies(int id) {
        HashMap<Integer, Movie> actor_movies = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            ArrayList<Integer> cast = movie.getValue().getCast();
            if(cast.contains(id))
                actor_movies.put(movie.getValue().getId(), movie.getValue());
        }
        return actor_movies;
    }

    public HashMap<Integer, Movie> getWatchList(String user_id) {
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_id);
        ArrayList<Integer> movie_ids = user.getWatchList();
        HashMap<Integer, Movie> movies = new HashMap<>();
        for (Integer movie_id : movie_ids) {
            Movie movie = null;
            movie = db.getMovieById(movie_id);
            movies.put(movie.getId(), movie);
        }
        return movies;
    }

    public void rateMovie(Integer movie_id, String user_email, int score){
        Database db = Database.getInstance();
        db.getUserByEmail(user_email);
        Movie movie = db.getMovieById(movie_id);
        movie.rate(user_email, score);
    }

    public void voteComment(String user_id, Integer comment_id, Integer vote) {
        Database db = Database.getInstance();
        db.getUserByEmail(user_id);
        Comment comment = db.getCommentById(comment_id);
        comment.vote(user_id, vote);
    }

    public void removeFromWatchList(String user_email, int movie_id) {
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_email);
        user.removeMovieToWatchList(movie_id);
    }

    public void addComment(int movie_id, String text) {
        Comment comment = new Comment();
        Database db = Database.getInstance();
        comment.setId(db.getComments().size() + 1);
        comment.setUserEmail(getLoggedInUser().getEmail());
        comment.setUserNickname(getLoggedInUser().getNickname());
        comment.setMovieId(movie_id);
        comment.setText(text);
        db.appendComment(comment);
        Movie movie = findMovieById(movie_id);
        movie.addComment(comment);
    }

    public List<Movie> getRecommendedMovies() {
        Database db = Database.getInstance();
        HashMap<Integer,Movie> movies = db.getMovies();
        HashMap<Integer, Movie> watchlist = new HashMap<>();
        try {
            watchlist = getWatchList(logged_in_user.getEmail());
        } catch (Exception ignored) {}
        List<Movie> recommended_movies = new ArrayList<>();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if(watchlist.containsKey(movie.getKey()))
                continue;
            movie.getValue().setRecommendationScore(watchlist);
            recommended_movies.add(movie.getValue());
        }
        recommended_movies = recommended_movies.stream().sorted(Comparator.comparing(Movie::getRecommendationScore).reversed()).toList();
        return recommended_movies.stream().limit(3).toList();
    }
}
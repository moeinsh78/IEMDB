package IEMDBClasses;

import Errors.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IEMDBSystem {
    public void importMovies(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<Movie> movieList = mapper.readValue(endpoints, new TypeReference<List<Movie>>() {});
        Database db = Database.getInstance();
        for (Movie movie : movieList) {
            db.appendMovie(movie);
        }
    }

    public void importActors(String endpoints) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<Actor> actorList = mapper.readValue(endpoints, new TypeReference<List<Actor>>() {
        });
        Database db = Database.getInstance();
        for (Actor actor : actorList) {
            db.appendActor(actor);
        }
        setActorsNamesForMovies();
    }

    public void importUsers(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<User> userList = mapper.readValue(endpoints, new TypeReference<List<User>>() {
        });
        Database db = Database.getInstance();
        for (User user : userList) {
            db.appendUser(user);
        }
    }

    public void importComments(String endpoints) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        List<Comment> commentList = mapper.readValue(endpoints, new TypeReference<List<Comment>>() {});

        Database db = Database.getInstance();
        for (Comment comment : commentList) {
            comment.setId(db.getComments().size() + 1);
            db.appendComment(comment);
            Movie movie = db.getMovieById(comment.getMovieId());
            movie.addComment(comment);
            comment.setUserNickname(db.getUsers().get(comment.getUserEmail()).getNickname());
        }
    }

    public HashMap<Integer, Movie> getMoviesList() {
        Database db = Database.getInstance();
        return db.getMovies();
    }

    public Movie findMovieById(int movie_id) throws MovieNotFoundError {
        Database db = Database.getInstance();
        return db.getMovieById(movie_id);
    }


    private void setActorsNamesForMovies() throws Exception{
        Database db = Database.getInstance();
        for (HashMap.Entry<Integer, Movie> movie_rec : db.getMovies().entrySet()) {
            Movie movie = movie_rec.getValue();
            ArrayList<String> cast_names = new ArrayList<>();
            for (Integer actor_id : movie.getCast())
                cast_names.add(db.getActorsById(actor_id).getName());

            movie.setCastNames(cast_names);
        }
    }

    public void addToWatchList(String user_email, Integer movie_id) throws  Exception{
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_email);
        Movie movie = db.getMovieById(movie_id);
        user.addMovieToWatchList(movie);
    }


    public HashMap<Integer, Movie> getMoviesByGenre(String genre){
        HashMap<Integer, Movie> movies_by_genre = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if(movie.getValue().getGenres().contains(genre))
                movies_by_genre.put(movie.getValue().getId(), movie.getValue());
        }
        return movies_by_genre;
    }

    public HashMap<Integer, Movie> getMoviesByYear(Integer start_year, Integer end_year){
        HashMap<Integer, Movie> movies_by_year = new HashMap<>();
        Database db = Database.getInstance();
        HashMap<Integer, Movie> movies = db.getMovies();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            String release_date = movie.getValue().getReleaseDate();
            String[] date = release_date.split("-");
            String release_year = date[0];
            Integer year = Integer.valueOf(release_year);
            if(year >= start_year && year <= end_year) {
                movies_by_year.put(movie.getValue().getId(), movie.getValue());
            }
        }
        return movies_by_year;
    }
    public Actor getActorById(int id) throws ActorNotFoundError {
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

    public HashMap<Integer, Movie> getWatchList(String user_id) throws Exception{
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_id);
        ArrayList<Integer> movie_ids = user.getWatchList();
        HashMap<Integer, Movie> movies = new HashMap<>();
        for (Integer movie_id : movie_ids) {
            Movie movie = null;
            try {
                movie = db.getMovieById(movie_id);
            } catch (Exception ignored) {}
            movies.put(movie.getId(), movie);
        }
        return movies;
    }

    public User getUser(String user_id) throws UserNotFoundError {
        Database db = Database.getInstance();
        return db.getUserByEmail(user_id);
    }

    public void rateMovie(Integer movie_id, String user_email, int score) throws Exception{
        Database db = Database.getInstance();
        db.getUserByEmail(user_email);
        Movie movie = db.getMovieById(movie_id);
        movie.rate(user_email, score);
    }

    public void voteComment(String user_id, Integer comment_id, Integer vote) throws Exception {
        Database db = Database.getInstance();
        db.getUserByEmail(user_id);
        Comment comment = db.getCommentById(comment_id);
        comment.vote(user_id, vote);
    }

    public void removeFromWatchList(String user_email, int movie_id) throws Exception {
        Database db = Database.getInstance();
        User user = db.getUserByEmail(user_email);
        user.removeMovieToWatchList(movie_id);
    }

}
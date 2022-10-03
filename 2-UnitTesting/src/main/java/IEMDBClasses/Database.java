package IEMDBClasses;

import Errors.*;
import java.util.HashMap;


public class Database {
    // Database Designed as a Singleton
    private HashMap<Integer, Actor> actors;
    private HashMap<String, User> users;
    private HashMap<Integer, Movie> movies;
    private HashMap<Integer, Comment> comments;

    private static Database single_instance = null;

    // Singleton Constructor

    private Database() {
        this.actors = new HashMap<Integer, Actor>();
        this.users = new HashMap<String, User>();
        this.movies = new HashMap<Integer, Movie>();
        this.comments = new HashMap<Integer, Comment>();
    }

    public static Database getInstance() {
        if (single_instance == null)
            single_instance = new Database();
        return single_instance;
    }

    public void appendActor(Actor new_actor){
        actors.put(new_actor.getId(), new_actor);
    }
    public void appendUser(User new_user){
        users.put(new_user.getEmail(), new_user);
    }
    public void appendMovie(Movie new_movie) {
        Movie prev_movie = movies.get(new_movie.getId());
        if (prev_movie != null) {
            new_movie.setRatings(prev_movie.getRatings());
            new_movie.setComments(prev_movie.getComments());
        }
        movies.put(new_movie.getId(), new_movie);
    }
    public void appendComment(Comment new_comment){
        comments.put(new_comment.getId(), new_comment);
    }

    public HashMap<String, User> getUsers(){
        return this.users;
    }

    public HashMap<Integer, Movie> getMovies(){
        return this.movies;
    }

    public HashMap<Integer, Comment> getComments(){
        return this.comments;
    }

    public Movie getMovieById(int id) throws MovieNotFoundError {
        Movie movie = movies.get(id);
        if (movie == null)
            throw new MovieNotFoundError();
        return movies.get(id);
    }


    public Actor getActorsById(int id) throws ActorNotFoundError {
        Actor actor = actors.get(id);
        if (actor == null)
            throw new ActorNotFoundError();
        return actor;
    }

    public User getUserByEmail(String email) throws UserNotFoundError {
        User user = users.get(email);
        if (user == null)
            throw new UserNotFoundError();
        return user;
    }

    public Comment getCommentById(int id) throws CommentNotFoundError {
        Comment comment =  comments.get(id);
        if(comment == null)
            throw new CommentNotFoundError();
        return comment;
    }

}
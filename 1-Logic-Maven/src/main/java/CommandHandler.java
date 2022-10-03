import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommandHandler {
    private ResponseHandler responseHandler = new ResponseHandler();
    public Database db = new Database();

    public String processCommand(String input_command) throws JsonProcessingException {
        String response = "";
        String command = "";
        String command_data_json = "";

        if (input_command.indexOf(' ') == -1) {
            command = input_command;
        } else {
            command = input_command.substring(0, input_command.indexOf(' '));
            command_data_json = input_command.substring(input_command.indexOf(' ') + 1);
        }
        ObjectMapper mapper = new ObjectMapper();
        Map input_data_map = null;

        if(!command_data_json.isEmpty())
            input_data_map = mapper.readValue(command_data_json, Map.class);

        assert input_data_map != null;
        switch (command) {
            case "addActor":
                response = addActor(input_data_map);
                break;
            case "addMovie":
                response = addMovie(input_data_map);
                break;
            case "addUser":
                response = addUser(input_data_map);
                break;
            case "addComment":
                response = addComment(input_data_map);
                break;
            case "rateMovie":
                response = rateMovie(input_data_map);
                break;
            case "voteComment":
                response = voteComment(input_data_map);
                break;
            case "addToWatchList":
                response = addToWatchList(input_data_map);
                break;
            case "removeFromWatchList":
                response = removeFromWatchList(input_data_map);
                break;
            case "getMoviesList":
                response = getMoviesList();
                break;
            case "getMovieById":
                response = getMovieById(input_data_map);;
                break;
            case "getMoviesByGenre":
                response = getMoviesByGenre(input_data_map);
                break;
            case "getWatchList":
                response = getWatchList(input_data_map);
                break;
            default:
                Exception e = new InvalidCommandError();
                response = responseHandler.commandFailedResponse(e);
                break;
        }

        return response;
    }

    public String addActor(Map input_data_map) throws JsonProcessingException {
        Actor new_actor = new Actor(
                (Integer) input_data_map.get("id"),
                (String) input_data_map.get("name"),
                (String) input_data_map.get("birthDate"),
                (String) input_data_map.get("nationality")
        );

        db.appendActor(new_actor);
        return responseHandler.addSuccessfullyResponse("actor");
    }

    public String addMovie(Map input_data_map) throws JsonProcessingException {
        ArrayList<Integer> new_movie_cast = (ArrayList<Integer>) input_data_map.get("cast");

        for (Integer actor_id: new_movie_cast) {
            try {
                db.getActorsById(actor_id);
            } catch(Exception e) {
                return responseHandler.commandFailedResponse(e);
            }
        }
        Movie new_movie = new Movie(
                (Integer) input_data_map.get("id"),
                (String) input_data_map.get("name"),
                (String) input_data_map.get("summary"),
                (String) input_data_map.get("releaseDate"),
                (String) input_data_map.get("director"),
                (ArrayList<String>) input_data_map.get("writers"),
                (ArrayList<String>) input_data_map.get("genres"),
                (ArrayList<Integer>) input_data_map.get("cast"),
                (Double) input_data_map.get("imdbRate"),
                (Integer) input_data_map.get("duration"),
                (Integer) input_data_map.get("ageLimit")
        );

        db.appendMovie(new_movie);
        return responseHandler.addSuccessfullyResponse("movie");
    }

    public String addUser(Map input_data_map) throws JsonProcessingException {
        String user_email = (String) input_data_map.get("email");
        try {
            db.getUserByEmail(user_email);
            Exception e2 = new UserAlreadyExistsError();
            return responseHandler.commandFailedResponse(e2);
        } catch (UserNotFoundError ignored){}

        User new_user = new User(
                (String) input_data_map.get("email"),
                (String) input_data_map.get("password"),
                (String) input_data_map.get("nickname"),
                (String) input_data_map.get("name"),
                (String) input_data_map.get("birthDate")
        );
        db.appendUser(new_user);
        return responseHandler.addSuccessfullyResponse("user");
    }

    public String addComment(Map input_data_map) throws JsonProcessingException {
        Integer movie_id = (Integer) input_data_map.get("movieId");
        String user_email = (String) input_data_map.get("userEmail");
        Movie movie = null;
        try {
            db.getUserByEmail(user_email);
            movie = db.getMovieById(movie_id);
        } catch (Exception e) {
            return responseHandler.commandFailedResponse(e);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        int new_comment_id = db.getComments().size() + 1;

        Comment new_comment = new Comment(
                new_comment_id,
                user_email,
                movie_id,
                (String) input_data_map.get("text"),
                dtf.format(now)
        );
        movie.addComment(new_comment);
        db.appendComment(new_comment);
        String added_object = "comment with id " + String.valueOf(new_comment_id);
        return responseHandler.addSuccessfullyResponse(added_object);
    }

    public String rateMovie(Map input_data_map) throws JsonProcessingException {
        int movie_id = (int) input_data_map.get("movieId");
        String user_email = (String) input_data_map.get("userEmail");

        try {
            int score = (int) input_data_map.get("score");
            try {
                db.getUserByEmail(user_email);
                Movie movie = db.getMovieById(movie_id);
                movie.rate(user_email, score);
            } catch (Exception e) {
                return responseHandler.commandFailedResponse(e);
            }
        } catch (ClassCastException e1) {
            Exception ex = new InvalidVoteScoreError();
            return responseHandler.commandFailedResponse(ex);
        } catch (NullPointerException e2) {
            Exception ex = new InvalidCommandError();
            return responseHandler.commandFailedResponse(ex);
        }
        return responseHandler.rateMovieSuccessfullyResponse();
    }

    public String voteComment(Map input_data_map) throws JsonProcessingException {
        int comment_id = (int) input_data_map.get("commentId");
        String user_email = (String) input_data_map.get("userEmail");
        try {
            int vote = (int) input_data_map.get("vote");
            try {
                db.getUserByEmail(user_email);
                Comment comment = db.getCommentById(comment_id);
                comment.vote(user_email, vote);
            } catch (Exception e) {
                return responseHandler.commandFailedResponse(e);
            }
        } catch (ClassCastException e1) {
            Exception ex = new InvalidVoteScoreError();
            return responseHandler.commandFailedResponse(ex);
        } catch (NullPointerException e2) {
            Exception ex = new InvalidCommandError();
            return responseHandler.commandFailedResponse(ex);
        }

        return responseHandler.voteCommentSuccessfullyResponse();
    }

    public String addToWatchList(Map input_data_map) throws JsonProcessingException {
        String user_email = (String) input_data_map.get("userEmail");
        int movie_id = (int) input_data_map.get("movieId");

        try {
            User user = db.getUserByEmail(user_email);
            Movie movie = db.getMovieById(movie_id);
            user.addMovieToWatchList(movie);
        } catch (Exception e) {
            return responseHandler.commandFailedResponse(e);
        }
        return responseHandler.addToWatchListSuccessfullyResponse();
    }

    public String removeFromWatchList(Map input_data_map) throws JsonProcessingException {
        String user_email = (String) input_data_map.get("userEmail");
        int movie_id = (int) input_data_map.get("movieId");

        try {
            User user = db.getUserByEmail(user_email);
            user.removeMovieToWatchList(movie_id);
        } catch (Exception e) {
            return responseHandler.commandFailedResponse(e);
        }
        return responseHandler.removeFromWatchListSuccessfullyResponse();
    }

    public String getMoviesList() throws JsonProcessingException{
        HashMap<Integer, Movie> movies = db.getMovies();
        return responseHandler.moviesListResponse(movies, "MoviesList");
    }

    public String getMovieById(Map input_data_map) throws JsonProcessingException {
        int movie_id = (int) input_data_map.get("movieId");
        try {
            Movie movie = db.getMovieById(movie_id);
            ArrayList<Actor> cast = new ArrayList<>();
            for (Integer actor_id : movie.getCast()) {
                Actor actor = null;
                try {
                    actor = db.getActorsById(actor_id);
                } catch (Exception ignored) {}
                cast.add(actor);
            }
            return responseHandler.movieResponse(movie, cast);
        } catch (Exception e) {
            return responseHandler.commandFailedResponse(e);
        }
    }

    public String getMoviesByGenre(Map input_data_map) throws JsonProcessingException{
        String genre = (String) input_data_map.get("genre");
        HashMap<Integer, Movie> movies = db.getMoviesByGenre(genre);
        return responseHandler.moviesListResponse(movies, "MoviesListByGenre");
    }

    public String getWatchList(Map input_data_map) throws JsonProcessingException{
        String user_email = (String) input_data_map.get("userEmail");
        User user = null;
        try {
            user = db.getUserByEmail(user_email);
        } catch (Exception e) {
            return responseHandler.commandFailedResponse(e);
        }
        ArrayList<Integer> movie_ids = user.getWatchList();
        HashMap<Integer, Movie> movies = new HashMap<>();
        for (Integer movie_id : movie_ids) {
            Movie movie = null;
            try {
                movie = db.getMovieById(movie_id);
            } catch (Exception ignored) {}
            movies.put(movie.getId(), movie);
        }
        return responseHandler.moviesListResponse(movies, "WatchList");
    }
}
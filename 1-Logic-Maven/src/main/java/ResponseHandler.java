import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;

public class ResponseHandler {
    public String addSuccessfullyResponse(String added_object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        String message = added_object + " added successfully";
        response.put("success", true);
        response.put("data", message);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }

    public String commandFailedResponse(Exception e) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", false);
        response.put("data", e.getMessage());
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
    public String rateMovieSuccessfullyResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        response.put("data", "movie rated successfully");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
    public String voteCommentSuccessfullyResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        response.put("data", "comment voted successfully");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
    public String addToWatchListSuccessfullyResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        response.put("data", "movie added to watchlist successfully");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
    public String removeFromWatchListSuccessfullyResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        response.put("data", "movie removed from watchlist successfully");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
    public String moviesListResponse(HashMap<Integer, Movie> movies, String list_name) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        ObjectNode moviesList = mapper.createObjectNode();
        ArrayNode moviesNode = moviesList.putArray(list_name);
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            ObjectNode details = mapper.createObjectNode();
            details.put("movieId", movie.getValue().getId());
            details.put("name", movie.getValue().getName());
            details.put("director", movie.getValue().getDirector());
            ArrayNode arrayNode = details.putArray("genres");
            for (String genre : movie.getValue().getGenres()) {
                arrayNode.add(genre);
            }
            try {
                details.put("rating", movie.getValue().getRatingsAverage());
            } catch (ArithmeticException ignored) {
                details.put("rating", (byte[]) null);
            }
            moviesNode.add(details);
        }
        response.set("data", moviesList);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }

    public String movieResponse(Movie movie, ArrayList<Actor> cast) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("success", true);
        ObjectNode details = mapper.createObjectNode();

        details.put("movieId", movie.getId());
        details.put("name", movie.getName());
        details.put("summary", movie.getSummary());
        details.put("releaseDate", movie.getReleaseDate());
        details.put("director", movie.getDirector());
        ArrayNode writerNode = details.putArray("writers");
        for (String writer : movie.getWriters()) {
            writerNode.add(writer);
        }
        ArrayNode genreNode = details.putArray("genres");
        for (String genre : movie.getGenres()) {
            genreNode.add(genre);
        }
        ArrayNode castNode = details.putArray("cast");
        for (Actor actor : cast) {
            ObjectNode actor_details = mapper.createObjectNode();
            actor_details.put("actorId", actor.getId());
            actor_details.put("name", actor.getName());
            castNode.add(actor_details);
        }
        try {
            details.put("rating", movie.getRatingsAverage());
        } catch (ArithmeticException ignored) {
            details.put("rating", (byte[]) null);
        }
        details.put("duration", movie.getDuration());
        details.put("ageLimit", movie.getAgeLimit());
        ArrayNode commentNode = details.putArray("comments");
        for (Comment comment : movie.getComments()) {
            ObjectNode comment_details = mapper.createObjectNode();
            comment_details.put("commentId", comment.getId());
            comment_details.put("userEmail", comment.getUserEmail());
            comment_details.put("text", comment.getText());
            HashMap<String, Integer> votes = comment.votesCount();
            comment_details.put("like", votes.get("like"));
            comment_details.put("dislike", votes.get("dislike"));
            commentNode.add(comment_details);
        }
        response.set("data", details);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        return json;
    }
}
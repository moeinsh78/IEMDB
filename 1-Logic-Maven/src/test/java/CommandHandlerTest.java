import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


public class CommandHandlerTest {
    private CommandHandler testCommandHandler;
    private Movie movie1, movie2;
    private Comment comment1;

    @Before
    public void setUp() {
        testCommandHandler = new CommandHandler();
        ArrayList<String> genres1 = new ArrayList<>();
        genres1.add("Crime");
        genres1.add("Music");
        movie1 = new Movie(1, "movie1", "something", "2021-01-01", "me",
                new ArrayList<>(), genres1, new ArrayList<>(), 9.5, 120, 12);

        ArrayList<String> genres2 = new ArrayList<>();
        genres2.add("Crime");
        genres2.add("Biography");
        movie2 = new Movie(2, "movie2", "something", "2021-02-01", "you",
                new ArrayList<>(), genres2, new ArrayList<>(), 8.0, 90, 10);

        User user1 = new User("hi@gmail.com", "1234",
                "Moein", "Moein", "2011-01-01");
        User user2 = new User("bi@gmail.com", "1234",
                "Atieh", "Atieh", "2012-01-01");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        comment1 = new Comment(1,"hi@gmail.com",1,"I like it", dtf.format(now));
        testCommandHandler.db.appendComment(comment1);
        testCommandHandler.db.appendMovie(movie1);
        testCommandHandler.db.appendMovie(movie2);
        testCommandHandler.db.appendUser(user1);
        testCommandHandler.db.appendUser(user2);
    }

    @Test
    public void invalidScoreRatingTest() throws Exception {
        String command = "rateMovie {\"userEmail\": \"hi@gmail.com\", \"movieId\": 1, \"score\": 12}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"Invalid value for rating the movie\"\n}");
    }

    @Test
    public void normalRatingCalculationTest() throws Exception{
        String command = "rateMovie {\"userEmail\": \"hi@gmail.com\", \"movieId\": 1, \"score\": 5}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : \"movie rated successfully\"\n}");
        assertEquals(movie1.getRatings().get("hi@gmail.com"),5);
    }

    @Test
    public void updatedRatingCalculationTest() throws Exception{
        String command = "rateMovie {\"userEmail\": \"hi@gmail.com\", \"movieId\": 1, \"score\": 5}";
        String command2 = "rateMovie {\"userEmail\": \"hi@gmail.com\", \"movieId\": 1, \"score\": 6}";
        String response = testCommandHandler.processCommand(command);
        String response2 = testCommandHandler.processCommand(command2);
        assertEquals(response2, "{\n  \"success\" : true,\n  \"data\" : \"movie rated successfully\"\n}");
        assertEquals(movie1.getRatings().get("hi@gmail.com"),6);
    }

    @Test
    public void invalidMovieTest() throws Exception{
        String command = "rateMovie {\"userEmail\": \"hi@gmail.com\", \"movieId\": 3, \"score\": 5}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"Movie with the given Id is not found\"\n}");
    }

    @Test
    public void invalidUserRatingTest() throws Exception{
        String command = "rateMovie {\"userEmail\": \"shi@gmail.com\", \"movieId\": 1, \"score\": 5}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"User with the given email is not found\"\n}");
    }

    @Test
    public void invalidVoteScoreTest() throws Exception{
        String command = "voteComment {\"userEmail\": \"hi@gmail.com\", \"commentId\": 1, \"vote\": 5}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"Invalid value for voting the comment\"\n}");
    }

    @Test
    public void normalVotingTest() throws Exception{
        String command = "voteComment {\"userEmail\": \"hi@gmail.com\", \"commentId\": 1, \"vote\": 1}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : \"comment voted successfully\"\n}");
        assertEquals(comment1.votesCount().get("like"),1);
    }

    @Test
    public void updatedVotingTest() throws Exception{
        String command = "voteComment {\"userEmail\": \"hi@gmail.com\", \"commentId\": 1, \"vote\": 1}";
        String command2 = "voteComment {\"userEmail\": \"hi@gmail.com\", \"commentId\": 1, \"vote\": -1}";
        String response = testCommandHandler.processCommand(command);
        String response2 = testCommandHandler.processCommand(command2);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : \"comment voted successfully\"\n}");
        assertEquals(comment1.votesCount().get("like"),0);
    }

    @Test
    public void invalidCommentTest() throws Exception{
        String command = "voteComment {\"userEmail\": \"hi@gmail.com\", \"commentId\": 2, \"vote\": 1}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"Comment with the given Id is not found\"\n}");
    }

    @Test
    public void invalidUserVotingTest() throws Exception{
        String command = "voteComment {\"userEmail\": \"shi@gmail.com\", \"commentId\": 1, \"vote\": 1}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"User with the given email is not found\"\n}");

    }

    @Test
    public void noMoviesFoundTest() throws Exception{
        String command = "getMoviesByGenre {\"genre\": \"Drama\"}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : {\n    \"MoviesListByGenre\" : [ ]\n  }\n}");
    }

    @Test
    public void genreInFirstElementTest() throws Exception{
        String command = "getMoviesByGenre {\"genre\": \"Crime\"}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : {\n    \"MoviesListByGenre\" : [ {\n      \"movieId\" : 1,\n      \"name\" : \"movie1\",\n      \"director\" : \"me\",\n      \"genres\" : [ \"Crime\", \"Music\" ],\n      \"rating\" : null\n    }, {\n      \"movieId\" : 2,\n      \"name\" : \"movie2\",\n      \"director\" : \"you\",\n      \"genres\" : [ \"Crime\", \"Biography\" ],\n      \"rating\" : null\n    } ]\n  }\n}");
    }

    @Test
    public void genreInFollowingElementsTest() throws Exception{
        String command = "getMoviesByGenre {\"genre\": \"Music\"}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : {\n    \"MoviesListByGenre\" : [ {\n      \"movieId\" : 1,\n      \"name\" : \"movie1\",\n      \"director\" : \"me\",\n      \"genres\" : [ \"Crime\", \"Music\" ],\n      \"rating\" : null\n    } ]\n  }\n}");
    }

    // add to watch list

    @Test
    public void ageUnderLimitTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"hi@gmail.com\", \"movieId\": 1}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"User's age is under the movie's age limit\"\n}");
    }

    @Test
    public void ageEqualToLimitTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"bi@gmail.com\", \"movieId\": 2}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"User's age is under the movie's age limit\"\n}");
    }

    @Test
    public void successfulAddToWatchListTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"hi@gmail.com\", \"movieId\": 2}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : true,\n  \"data\" : \"movie added to watchlist successfully\"\n}");
    }

    @Test
    public void movieAlreadyInWatchListTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"hi@gmail.com\", \"movieId\": 2}";
        String command2 = "addToWatchList {\"userEmail\": \"hi@gmail.com\", \"movieId\": 2}";
        String response = testCommandHandler.processCommand(command);
        String response2 = testCommandHandler.processCommand(command2);
        assertEquals(response2, "{\n  \"success\" : false,\n  \"data\" : \"The movie has already been added to the watchlist\"\n}");
    }

    @Test
    public void invalidMovieInWatchListTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"hi@gmail.com\", \"movieId\": 5}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"Movie with the given Id is not found\"\n}");
    }

    @Test
    public void invalidUserInWatchListTest() throws Exception{
        String command = "addToWatchList {\"userEmail\": \"shi@gmail.com\", \"movieId\": 1}";
        String response = testCommandHandler.processCommand(command);
        assertEquals(response, "{\n  \"success\" : false,\n  \"data\" : \"User with the given email is not found\"\n}");
    }

    @After
    public void tearDown() {
        testCommandHandler = null;
        movie1 = null;
        comment1 = null;
        movie2 = null;
    }
}
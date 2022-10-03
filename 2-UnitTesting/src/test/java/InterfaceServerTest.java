import IEMDBClasses.Database;
import IEMDBClasses.Movie;
import InterfaceServer.InterfaceServer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class InterfaceServerTest {
    private InterfaceServer interfaceServer;
    String movies_address = "http://138.197.181.131:5000/api/movies";
    String actors_address = "http://138.197.181.131:5000/api/actors";
    String users_address = "http://138.197.181.131:5000/api/users";
    String comments_address = "http://138.197.181.131:5000/api/comments";

    int infoServerPort = 8080;
    private Database db;

    @Before
    public void setUp() {
        db = Database.getInstance();
        interfaceServer = new InterfaceServer();
        try {
            interfaceServer.start(movies_address, actors_address, users_address, comments_address, infoServerPort);
        } catch (Exception ignored) {}
    }

    @Test
    public void invalidScoreRatingTest() throws Exception {
        this.interfaceServer.rateMovieTest("mahdiye@gmail.com",1,12);
        assertTrue(db.getMovieById(7).getRatings().isEmpty());
    }

    @Test
    public void normalRatingCalculationTest() throws Exception{
        this.interfaceServer.rateMovieTest("mahdiye@gmail.com",7,5);
        assertEquals(5, (int) db.getMovieById(7).getRatings().get("mahdiye@gmail.com"));
    }

    @Test
    public void updatedRatingCalculationTest() throws Exception{
        this.interfaceServer.rateMovieTest("mahdiye@gmail.com",7,5);
        this.interfaceServer.rateMovieTest("mahdiye@gmail.com",7,9);
        assertEquals(9, (int) db.getMovieById(7).getRatings().get("mahdiye@gmail.com"));
    }

    @Test
    public void getWatchListSuccessful() throws Exception{
        this.interfaceServer.addToWatchListTest("mahdiye@gmail.com",5);
        this.interfaceServer.addToWatchListTest("mahdiye@gmail.com",7);
        HashMap<Integer, Movie> actual_movies = this.interfaceServer.getWatchListTest("mahdiye@gmail.com");
        assertTrue(actual_movies.containsKey(5));
        assertTrue(actual_movies.containsKey(7));
    }

    @Test
    public void getWatchListUserNotFoundFail() {
        this.interfaceServer.addToWatchListTest("mahdiye@gmail.com",7);
        try {
            this.interfaceServer.getWatchListTest("mahdiyeh@gmail.com");
        } catch (Exception e) {
            assertEquals("User with the given email is not found", e.getMessage());
        }
    }

    @Test
    public void SearchMovieByYearNoMovieSuccessful() {
        HashMap<Integer, Movie> actual_movies = this.interfaceServer.searchMovieByYearTest(1,2);
        assertTrue(actual_movies.isEmpty());
    }

    @Test
    public void SearchMovieByYearSuccessful() {
        HashMap<Integer, Movie> actual_movies = this.interfaceServer.searchMovieByYearTest(1999,2006);
        assertEquals(4, actual_movies.size());
    }

    @Test
    public void SearchMovieByYearWrongCommandFail() {
        assertNull(this.interfaceServer.searchMovieByYearTest(2006, 1999));
    }
}
package InterfaceServer;

import Errors.*;
import IEMDBClasses.Actor;
import IEMDBClasses.IEMDBSystem;
import IEMDBClasses.Movie;
import IEMDBClasses.User;
import io.javalin.Javalin;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class InterfaceServer {
    private final IEMDBSystem system = new IEMDBSystem();
    private final HTMLHandler html_handler = new HTMLHandler();

    public void start(String movies_address, String actors_address, String users_address,
                      String comments_address, int server_port) throws Exception {
        String movies_endpoints = getEndPoints(movies_address);
        system.importMovies(movies_endpoints);
        String actors_endpoints = getEndPoints(actors_address);
        system.importActors(actors_endpoints);
        String users_endpoints = getEndPoints(users_address);
        system.importUsers(users_endpoints);
        String comments_endpoints = getEndPoints(comments_address);
        system.importComments(comments_endpoints);
        runServer(server_port);
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

    private void runServer(int server_port) {
        Javalin server = Javalin.create().start(server_port);

        server.get("/movies" , ctx -> ctx.html(html_handler.createMoviesHTML(system.getMoviesList())));

        server.get("/movie/:movie_id" , ctx -> {
            Movie movie;
            try {
                movie = system.findMovieById(Integer.parseInt(ctx.pathParam("movie_id")));
                ctx.html(html_handler.createMovieInfoHTML(movie));
            } catch (Exception e) {
                ctx.redirect("/404");
            }
        });

        server.get("/movies/search/:genre" , ctx -> {
            HashMap<Integer, Movie> movies = this.system.getMoviesByGenre(ctx.pathParam("genre"));
            ctx.html(this.html_handler.createMoviesHTML(movies));
        });

        server.get("/movies/search/:start_year/:end_year" , ctx -> {
            if(Integer.parseInt(ctx.pathParam("start_year")) > Integer.parseInt(ctx.pathParam("end_year"))){
                ctx.redirect("/403");
            }
            HashMap<Integer, Movie> movies = this.system.getMoviesByYear(Integer.valueOf(ctx.pathParam("start_year")),
                    Integer.valueOf(ctx.pathParam("end_year")));
            ctx.html(this.html_handler.createMoviesHTML(movies));
        });

        server.get("/actors/:actor_id" , ctx -> {
            Actor actor;
            try {
                actor = this.system.getActorById(Integer.parseInt(ctx.pathParam("actor_id")));
                HashMap<Integer, Movie> movies = this.system.getActorMovies(actor.getId());
                ctx.html(this.html_handler.createActorHTML(actor, movies));
            } catch (Exception e) {
                ctx.redirect("/404");
            }
        });

        server.get("/watchList/:user_id/:movie_id" , ctx -> {
            try {
                this.system.addToWatchList(ctx.pathParam("user_id"), Integer.parseInt(ctx.pathParam("movie_id")));
                ctx.redirect("/successful_operation");
            } catch (AgeLimitError | MovieAlreadyExistsError e) {
                ctx.redirect("/403");
            } catch (Exception e2) {
                ctx.redirect("/404");
            }
        });

        server.get("/rateMovie/:user_id/:movie_id/:rate" , ctx -> {
            try {
                this.system.rateMovie(Integer.parseInt(ctx.pathParam("movie_id")),
                        ctx.pathParam("user_id"), Integer.parseInt(ctx.pathParam("rate")));
                ctx.redirect("/successful_operation");
            } catch (InvalidRateScoreError e1) {
                ctx.redirect("/403");
            } catch (Exception e2) {
                ctx.redirect("/404");
            }
        });

        server.get("/voteComment/:user_id/:comment_id/:vote" , ctx -> {
            try {
                this.system.voteComment(ctx.pathParam("user_id"),
                        Integer.parseInt(ctx.pathParam("comment_id")), Integer.parseInt(ctx.pathParam("vote")));
                ctx.redirect("/successful_operation");
            }  catch (InvalidVoteScoreError e1) {
                ctx.redirect("/403");
            } catch (Exception e2) {
                ctx.redirect("/404");
            }
        });

        server.get("/watchList/:user_id" , ctx -> {
            try {
                HashMap<Integer, Movie> movies = this.system.getWatchList(ctx.pathParam("user_id"));
                User user = this.system.getUser(ctx.pathParam("user_id"));
                ctx.html(this.html_handler.createWatchListHTML(user, movies));
            } catch (Exception e) {
                ctx.redirect("/404");
            }
        });

        server.post("/watchList" , ctx -> {
            String redirection_path = "/watchList/" + ctx.formParam("user_id") + "/" + ctx.formParam("movie_id");
            ctx.redirect(redirection_path);
        });

        server.post("/rateMovie" , ctx -> {
            String redirection_path = "/rateMovie/" + ctx.formParam("user_id") + "/" +
                    ctx.formParam("movie_id") + "/" + ctx.formParam("quantity");
            ctx.redirect(redirection_path);
        });

        server.post("/voteComment" , ctx -> {
            String vote = null;
            if((ctx.formParam("like") == null) && (ctx.formParam("dislike") != null)) {
                vote = "-1";
            } else if ((ctx.formParam("like") != null) && (ctx.formParam("dislike") == null)) {
                vote = "1";
            } else {
                ctx.redirect("/403");
            }
            String redirection_path = "/voteComment/" + ctx.formParam("user_id") + "/" +
                    ctx.formParam("comment_id") + "/" + vote;
            ctx.redirect(redirection_path);
        });

        server.post("/removeFromWatchList" , ctx -> {
            String user_email = ctx.formParam("user_id");
            int movie_id = Integer.parseInt(Objects.requireNonNull(ctx.formParam("movie_id")));
            try {
                this.system.removeFromWatchList(user_email, movie_id);
                ctx.redirect("/watchList/" + user_email);
            } catch (Exception ignored){}
        });

        server.get("/successful_operation" , ctx -> ctx.html(html_handler.getResponseCodeHtml("200")));
        server.get("/404" , ctx -> ctx.html(html_handler.getResponseCodeHtml("404")));
        server.get("/403" , ctx -> ctx.html(html_handler.getResponseCodeHtml("403")));
    }
    public void rateMovieTest(String user_id, int movie_id, int rate){
        try {
            this.system.rateMovie(movie_id,user_id,rate);
        } catch (Exception ignored) { }
    }

    public HashMap<Integer, Movie> getWatchListTest(String user_id) throws Exception {
        return this.system.getWatchList(user_id);
    }

    public void addToWatchListTest(String user_id, int movie_id) {
        try {
            this.system.addToWatchList(user_id, movie_id);
        } catch (Exception ignored) { }

    }

    public HashMap<Integer, Movie> searchMovieByYearTest(int start_year, int end_year) {
        if(start_year > end_year)
            return null;
        return this.system.getMoviesByYear(start_year,end_year);
    }

}

package com.ie.CA8.Service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ie.CA8.Entity.*;
import com.ie.CA8.Errors.*;
import com.ie.CA8.Repository.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class IEMDBSystem {
    private User logged_in_user;
    private final ActorRepository actor_repository;
    private final MovieRepository movie_repository;
    private final CommentRepository comment_repository;
    private final RateRepository rate_repository;
    private VoteRepository vote_repository;
    private final UserRepository user_repository;

    @Autowired
    private IEMDBSystem(ActorRepository actor_rep, MovieRepository movie_rep, VoteRepository vote_repo,
                        CommentRepository comment_rep, RateRepository rate_rep, UserRepository user_rep) {
        actor_repository = actor_rep;
        movie_repository = movie_rep;
        comment_repository = comment_rep;
        rate_repository = rate_rep;
        user_repository = user_rep;
        vote_repository = vote_repo;
        setDB(
                "http://138.197.181.131:5000/api/v2/movies",
                "http://138.197.181.131:5000/api/v2/actors",
                "http://138.197.181.131:5000/api/users"
        );
    }


    public void setDB(String movies_address, String actors_address, String users_address) {
        if (!movie_repository.findAll().isEmpty())
            return;
        try {
            String movies_endpoints = getEndPoints(movies_address);
            List<Movie> movies = importMovies(movies_endpoints);
            String actors_endpoints = getEndPoints(actors_address);
            List<Actor> actors = importActors(actors_endpoints);
            String users_endpoints = getEndPoints(users_address);
            List<User> users = importUsers(users_endpoints);
            for (Movie movie : movies) {
                List<Integer> cast = movie.getCast();
                for (Actor actor : actors) {
                    if (cast.contains(actor.getId())) {
                        movie.setActor(actor);
                    }
                }
            }
            actor_repository.saveAll(actors);
            movie_repository.saveAll(movies);
            user_repository.saveAll(users);
            hashUsersPasswords();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    private void hashUsersPasswords() {
        List<User> users = user_repository.findAll();
        for (User user: users) {
            String hashed_password = String.valueOf(user.getPassword().hashCode());
            user.setPassword(hashed_password);
            user_repository.save(user);
        }
    }

    private String getEndPoints(String url) {
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

    public List<Movie> importMovies(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper.readValue(endpoints, new TypeReference<>() {});
    }

    public List<User> importUsers(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper.readValue(endpoints, new TypeReference<>() {
        });
    }

    public void updateUser(String name, String user_email, String password, String nickname, String birth_date) {
        Optional<User> existing_user = user_repository.findById(user_email);

        String hashed_password = String.valueOf(password.hashCode());
        User user = existing_user.get();
        user.setName(name);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setBirthDate(birth_date);
        user_repository.save(user);
    }

    public List<Actor> importActors(String endpoints) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper.readValue(endpoints, new TypeReference<>() {
        });
    }

    public User getLoggedInUser() {
        return logged_in_user;
    }

    public String login(String user_email, String password) throws UserNotFoundError {
        Optional<User> user = user_repository.findById(user_email);
        String hashed_password = String.valueOf(password.hashCode());
        if (user.stream().findFirst().isEmpty())
            throw new UserNotFoundError();
        if (hashed_password.equals(user.get().getPassword()))
            this.logged_in_user = user.get();
        else
            throw new UserNotFoundError();
        return createJwtToken(user_email);
    }

    public void loginWithJwtToken(String user_email) {
        Optional<User> user = user_repository.findById(user_email);
        this.logged_in_user = user.get();
    }

    public String createJwtToken(String user_email) {
        String sign_key = "---------------iemdb1401---------------";

//        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(hmac_secret_key), SignatureAlgorithm.HS256.getJcaName());

        SecretKey signature_type = new SecretKeySpec(sign_key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        String jwt_token = Jwts.builder()
                .claim("userEmail", user_email)
                .setId(UUID.randomUUID().toString())
                .setIssuer("IEMDB_SYSTEM")                                                      // iss claim
                .setIssuedAt(Date.from(Instant.now()))                                          // iat claim
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS))) // exp claim
                .signWith(signature_type)
                .compact();
        return jwt_token;
    }

    public void signup(String name, String user_email, String password, String nickname, String birth_date)
            throws UserAlreadyExistsError {
        Optional<User> existing_user = user_repository.findById(user_email);

        if (existing_user.stream().findFirst().isPresent())
            throw new UserAlreadyExistsError();
        User new_user = new User();
        String hashed_password = String.valueOf(password.hashCode());

        new_user.setName(name);
        new_user.setEmail(user_email);
        new_user.setPassword(hashed_password);
        new_user.setNickname(nickname);
        new_user.setBirthDate(birth_date);

        user_repository.save(new_user);
    }

    public void logout() {
        this.logged_in_user = null;
    }

    public List<Movie> getMoviesList() {
        return movie_repository.findAll();
    }

    public List<Movie> getMoviesByName(String name) {
        return movie_repository.findByNameContainingIgnoreCase(name);
    }

    public List<Movie> getMoviesByReleaseDate(String releaseDate) {
        return movie_repository.findByReleaseDateLike(releaseDate + "/%");
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movie_repository.findByGenres(genre);
    }

    public List<Movie> getMoviesByNameSortByImdb(String name) {
        return movie_repository.findByNameContainingIgnoreCaseOrderByImdbRateDesc(name);
    }

    public List<Movie> getMoviesByNameSortByReleaseDate(String name) {
        return movie_repository.findByNameContainingIgnoreCaseOrderByReleaseDateDesc(name);
    }

    public List<Movie> getMoviesByReleaseDateSortByImdb(String releaseDate) {
        return movie_repository.findByReleaseDateLikeOrderByImdbRateDesc(releaseDate + "/%");
    }

    public List<Movie> getMoviesByReleaseDateSortByReleaseDate(String releaseDate) {
        return movie_repository.findByReleaseDateLikeOrderByReleaseDateDesc(releaseDate + "/%");
    }

    public List<Movie> getMoviesByGenreSortByImdb(String genre) {
        return movie_repository.findByGenresOrderByImdbRateDesc(genre);
    }

    public List<Movie> getMoviesByGenreSortByReleaseDate(String genre) {
        return movie_repository.findByGenresOrderByReleaseDateDesc(genre);
    }

    public List<Movie> getMoviesOrderByImdb() {
        return movie_repository.findByOrderByImdbRateDesc();
    }

    public List<Movie> getMoviesOrderByReleaseDate() {
        return movie_repository.findByOrderByReleaseDateDesc();
    }

    public Movie findMovieById(int movie_id) {
        Optional<Movie> m = movie_repository.findById(movie_id);
        Movie movie;
        try {
            movie = m.get();
            return movie;
        } catch (Exception e) {
            return null;
        }
    }

    public void addToWatchList(String user_email, Integer movie_id) {
        Optional<Movie> m = movie_repository.findById(movie_id);
        Movie movie;
        try {
            movie = m.get();
        } catch (Exception e) {
            return;
        }
        Optional<User> u = user_repository.findById(user_email);
        User user;
        try {
            user = u.get();
        } catch (Exception e) {
            return;
        }
        user.addMovieToWatchList(movie);
        user_repository.save(user);
    }

    public Actor getActorById(int actor_id) {
        Optional<Actor> actor = actor_repository.findById(actor_id);
        // Error Handling Actor Not Found
        return actor.get();
    }

    public HashMap<Integer, Movie> getActorMovies(int actor_id) {
        Optional<Actor> actor_DAO = actor_repository.findById(actor_id);
        // Error Handling Actor Not Found
        Actor actor = actor_DAO.get();
        System.out.println(actor_id);
        List<Movie> actor_movies = movie_repository.findByActors(actor);
        HashMap<Integer, Movie> actor_movies_map = new HashMap<>();
        for (Movie movie : actor_movies) {
            actor_movies_map.put(movie.getId(), movie);
        }
        return actor_movies_map;
    }

    public void addComment(int movie_id, String text) {
        Comment comment = new Comment();
        comment.setUserNickname(getLoggedInUser().getNickname());
        comment.setText(text);
        comment_repository.save(comment);
        Movie movie = findMovieById(movie_id);
        movie.addComment(comment);
        movie_repository.save(movie);
    }

    public void rateMovie(Integer movie_id, int score) {
        Rate rate = rate_repository.findByUserIdAndMovieId(getLoggedInUser().getEmail(), movie_id);
        Optional<Movie> mov = movie_repository.findById(movie_id);
        Movie movie = mov.get();
        if (rate == null) {
            Rate new_rate = new Rate();
            new_rate.setScore(score);
            new_rate.setMovieId(movie_id);
            rate_repository.save(new_rate);
            movie.rate(new_rate);
            movie_repository.save(movie);
        } else {
            rate.setScore(score);
            rate_repository.save(rate);
        }

    }

    public List<Movie> getRecommendedMovies() {
        Optional<User> user_DAO = user_repository.findById(logged_in_user.getEmail());
        Set<Movie> user_watchlist = user_DAO.get().getWatchList();
        List<Movie> all_movies = movie_repository.findAll();

        HashMap<Integer, Movie> movies = new HashMap<>();
        HashMap<Integer, Movie> watchlist = new HashMap<>();

        for (Movie movie : all_movies)
            movies.put(movie.getId(), movie);

        for (Movie movie : user_watchlist)
            watchlist.put(movie.getId(), movie);

        List<Movie> recommended_movies = new ArrayList<>();
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            if (watchlist.containsKey(movie.getKey()))
                continue;
            movie.getValue().setRecommendationScore(watchlist);
            recommended_movies.add(movie.getValue());
        }
        recommended_movies = recommended_movies.stream().sorted(Comparator.comparing(Movie::getRecommendationScore).reversed()).toList();
        return recommended_movies.stream().limit(3).toList();
    }

    public void removeFromWatchList(String user_email, int movie_id) {
        Optional<User> user_DAO = user_repository.findById(user_email);
        Optional<Movie> movie_DAO = movie_repository.findById(movie_id);
        User user = user_DAO.get();
        Movie movie = movie_DAO.get();
        user.removeMovieFromWatchList(movie);
        user_repository.save(user);
    }

    public Set<Movie> getWatchList(String user_id) {
        Optional<User> user = user_repository.findById(user_id);

        return user.get().getWatchList();
    }


    public void likeComment(Integer comment_id) {
        Vote vote = vote_repository.findByUserIdAndCommentId(getLoggedInUser().getEmail(), comment_id);
        Optional<Comment> com = comment_repository.findById(comment_id);
        Comment comment = com.get();
        if (vote == null) {
            Vote new_vote = new Vote();
            new_vote.setVote(1);
            new_vote.setCommentId(comment_id);
            new_vote.setUserId(getLoggedInUser().getEmail());
            vote_repository.save(new_vote);
            comment.addVote(new_vote);
            comment.setLikes(1);
            comment_repository.save(comment);
        } else {
            if (vote.getVote() == -1) {
                vote.setVote(1);
                comment.setLikes(1);
                comment.setDislikes(-1);
                vote_repository.save(vote);
            }
        }
        comment_repository.save(comment);
    }

    public void dislikeComment(Integer comment_id) {
        Vote vote = vote_repository.findByUserIdAndCommentId(getLoggedInUser().getEmail(), comment_id);
        Optional<Comment> com = comment_repository.findById(comment_id);
        Comment comment = com.get();
        if (vote == null) {
            Vote new_vote = new Vote();
            new_vote.setVote(-1);
            new_vote.setCommentId(comment_id);
            new_vote.setUserId(getLoggedInUser().getEmail());
            vote_repository.save(new_vote);
            comment.addVote(new_vote);
            comment.setDislikes(1);
        } else {
            if (vote.getVote() == 1) {
                vote.setVote(-1);
                comment.setDislikes(1);
                comment.setLikes(-1);
                vote_repository.save(vote);
            }
        }
        comment_repository.save(comment);
    }
}
package com.ie.CA6.Repository;

import com.ie.CA6.Entity.Actor;
import com.ie.CA6.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByActors(Actor actor);

    List<Movie> findByNameContainingIgnoreCase(String name);

    List<Movie> findByReleaseDateLike(String releaseDate);

    List<Movie> findByGenres(String genre);

    List<Movie> findByNameContainingIgnoreCaseOrderByImdbRateDesc(String name);

    List<Movie> findByNameContainingIgnoreCaseOrderByReleaseDateDesc(String name);

    List<Movie> findByReleaseDateLikeOrderByImdbRateDesc(String releaseDate);

    List<Movie> findByReleaseDateLikeOrderByReleaseDateDesc(String releaseDate);

    List<Movie> findByGenresOrderByImdbRateDesc(String genre);

    List<Movie> findByGenresOrderByReleaseDateDesc(String genre);

    List<Movie> findByOrderByImdbRateDesc();

    List<Movie> findByOrderByReleaseDateDesc();

//    public default ArrayList<Movie> findMoviesByActor(int actor_id) {
//
//    }
}

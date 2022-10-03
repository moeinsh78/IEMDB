package com.ie.CA7.Controller;

import com.ie.CA7.Entity.Actor;
import com.ie.CA7.Service.IEMDBSystem;
import com.ie.CA7.Entity.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.Map;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MovieController {
    @Autowired
    private IEMDBSystem iemdbSystem;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movie/{id}", method = RequestMethod.GET)
    public Movie getMovie(@PathVariable("id") String id){
        int movie_id = Integer.parseInt(id);
        return iemdbSystem.findMovieById(movie_id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movie/{id}/cast", method = RequestMethod.GET)
    public Set<Actor> getMovieCast(@PathVariable("id") String id){
        int movie_id = Integer.parseInt(id);
        return iemdbSystem.findMovieById(movie_id).getActors();
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت به کامنت ها اضافه شد.")
    @RequestMapping(value = "/movie/add_comment",method = RequestMethod.POST)
    public void addComment(@RequestBody Map<String, String> comment_info) {
            int movie_id = Integer.parseInt(comment_info.get("movieId"));
            iemdbSystem.addComment(movie_id,comment_info.get("commentTxt"));
        }

    @ResponseStatus(value = HttpStatus.OK,reason = "امتیاز با موفقیت داده شد.")
    @RequestMapping(value = "/movie/rate",method = RequestMethod.POST)
    public void rateMovie(@RequestBody Map<String, String> body){
            int movie_id = Integer.parseInt(body.get("movieId"));
            iemdbSystem.rateMovie(movie_id, Integer.parseInt(body.get("score")));
        }

    @ResponseStatus(value = HttpStatus.OK,reason = "فیلم با موفقیت به واچ‌لیست اضافه شد.")
    @RequestMapping(value = "/movie/add_to_watchlist",method = RequestMethod.POST)
    public void addToWatchList(@RequestBody Map<String, String> movieId) {
        int movie_id = Integer.parseInt(movieId.get("movieId"));
        iemdbSystem.addToWatchList(iemdbSystem.getLoggedInUser().getEmail(), movie_id);
    }
}

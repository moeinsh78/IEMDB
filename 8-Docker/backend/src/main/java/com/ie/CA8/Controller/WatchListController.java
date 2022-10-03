package com.ie.CA8.Controller;

import com.ie.CA8.Entity.Movie;
import com.ie.CA8.Service.IEMDBSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WatchListController{
    @Autowired
    private IEMDBSystem iemdbSystem;


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/watchlist", method = RequestMethod.GET)
    public Set<Movie> getWatchList(){
        return iemdbSystem.getWatchList(iemdbSystem.getLoggedInUser().getEmail());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/watchlist/recommendation", method = RequestMethod.GET)
    public List<Movie> getRecommendedMovies(){
        return iemdbSystem.getRecommendedMovies();
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "فیلم با موفقیت از واچ لیست حذف شد.")
    @RequestMapping(value = "/watchlist/remove_from_watchlist",method = RequestMethod.POST)
    public void removeFromWatchList(@RequestBody Map<String, String> user_info){
        int movie_id = Integer.parseInt(user_info.get("movieId"));
        iemdbSystem.removeFromWatchList(iemdbSystem.getLoggedInUser().getEmail(), movie_id);
    }
}

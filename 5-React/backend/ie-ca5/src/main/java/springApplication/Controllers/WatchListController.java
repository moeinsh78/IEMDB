package springApplication.Controllers;

import springApplication.IEMDBClasses.IEMDBSystem;
import springApplication.IEMDBClasses.Movie;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WatchListController{
    private IEMDBSystem iemdbSystem;

    public WatchListController(){
        iemdbSystem = IEMDBSystem.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/watchlist", method = RequestMethod.GET)
    public HashMap<Integer, Movie> getWatchList(){
        return iemdbSystem.getWatchList(iemdbSystem.getLoggedInUser().getEmail());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/watchlist/recommendation", method = RequestMethod.GET)
    public List<Movie> getRecommendedMovies(){
        return iemdbSystem.getRecommendedMovies();
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "فیلم با موفقیت به واچ لیست اضافه شد.")
    @RequestMapping(value = "/watchlist/remove_from_watchlist",method = RequestMethod.POST)
    public void removeFromWatchList(@RequestBody Map<String, String> user_info){
        int movie_id = Integer.parseInt(user_info.get("movieId"));
        iemdbSystem.removeFromWatchList(iemdbSystem.getLoggedInUser().getEmail(), movie_id);
    }
}

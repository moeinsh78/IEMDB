package springApplication.Controllers;

import springApplication.IEMDBClasses.IEMDBSystem;
import springApplication.IEMDBClasses.Movie;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MoviesController{
    private IEMDBSystem iemdbSystem;

    public MoviesController(){
        iemdbSystem = IEMDBSystem.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public HashMap<Integer, Movie> getMovies(){
        return iemdbSystem.getMoviesList();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies/sort",method = RequestMethod.GET)
    public List<Movie> sortMovies(
        @RequestParam("sortMethod") String sort_method,
        @RequestParam(value = "searchMethod", required = false) String search_method,
        @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        HashMap<Integer, Movie> movies = new HashMap<>();

        if(search_method != null && !searched_txt.equals("")) {
            if (search_method.equals("movieName")) {
                movies = iemdbSystem.getMoviesByName(searched_txt);
            } else if (search_method.equals("releaseDate")) {
                movies = iemdbSystem.getMoviesByReleaseDate(searched_txt);
            } else if (search_method.equals("genre")) {
                movies = iemdbSystem.getMoviesByGenre(searched_txt);
            }
        }
        else {
            movies = iemdbSystem.getMoviesList();
        }
        List<Movie> movies_list = movies.values().stream().toList();
        if(sort_method.equals("sort_by_imdb")) {
            movies_list = movies_list.stream().sorted(Comparator.comparing(Movie::getImdbRate).reversed()).toList();
        }
        if(sort_method.equals("sort_by_date")){
            movies_list = movies_list.stream().sorted(Comparator.comparing(Movie::getReleaseDate).reversed()).toList();
        }

        return movies_list;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies/search",method = RequestMethod.GET)
    public HashMap<Integer, Movie> searchMovies(
        @RequestParam(value = "searchMethod", required = false) String search_method,
        @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        HashMap<Integer, Movie> movies_list = new HashMap<>();
        if(search_method == null || searched_txt.equals("")) {
            return iemdbSystem.getMoviesList();
        }
        if(search_method.equals("movieName")) {
            movies_list = iemdbSystem.getMoviesByName(searched_txt);
        }
        else if(search_method.equals("releaseDate")) {
            movies_list = iemdbSystem.getMoviesByReleaseDate(searched_txt);
        }
        else if(search_method.equals("genre")) {
            movies_list = iemdbSystem.getMoviesByGenre(searched_txt);
        }
        return movies_list;
    }
    
}

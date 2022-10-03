package com.ie.CA7.Controller;

import com.ie.CA7.Service.IEMDBSystem;
import com.ie.CA7.Entity.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MoviesController{

    @Autowired
    private IEMDBSystem iemdbSystem;


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public List<Movie> getMovies(){
        return iemdbSystem.getMoviesList();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies/search",method = RequestMethod.GET)
    public List<Movie> searchMovies(
            @RequestParam(value = "searchMethod", required = false) String search_method,
            @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        List <Movie> movies_list = new ArrayList<>();
        System.out.println(searched_txt);
        if(search_method == null || searched_txt.equals("")) {
            return iemdbSystem.getMoviesList();
        }
        switch (search_method) {
            case "movieName" -> movies_list = iemdbSystem.getMoviesByName(searched_txt);
            case "releaseDate" -> movies_list = iemdbSystem.getMoviesByReleaseDate(searched_txt);
            case "genre" -> movies_list = iemdbSystem.getMoviesByGenre(searched_txt);
        }
        return movies_list;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/movies/sort",method = RequestMethod.GET)
    public List<Movie> sortMovies(
            @RequestParam("sortMethod") String sort_method,
            @RequestParam(value = "searchMethod", required = false) String search_method,
            @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        List <Movie> movies = new ArrayList<>();

        if(search_method != null && !searched_txt.equals("")) {
            switch (search_method) {
                case "movieName" -> {
                    if (sort_method.equals("sort_by_imdb"))
                        movies = iemdbSystem.getMoviesByNameSortByImdb(searched_txt);
                    else if (sort_method.equals("sort_by_date"))
                        movies = iemdbSystem.getMoviesByNameSortByReleaseDate(searched_txt);
                }
                case "releaseDate" -> {
                    if (sort_method.equals("sort_by_imdb"))
                        movies = iemdbSystem.getMoviesByReleaseDateSortByImdb(searched_txt);
                    else if (sort_method.equals("sort_by_date"))
                        movies = iemdbSystem.getMoviesByReleaseDateSortByReleaseDate(searched_txt);
                }
                case "genre" -> {
                    if (sort_method.equals("sort_by_imdb"))
                        movies = iemdbSystem.getMoviesByGenreSortByImdb(searched_txt);
                    else if (sort_method.equals("sort_by_date"))
                        movies = iemdbSystem.getMoviesByGenreSortByReleaseDate(searched_txt);
                }
            }
        }
        else {
            if (sort_method.equals("sort_by_imdb"))
                movies = iemdbSystem.getMoviesOrderByImdb();
            else if (sort_method.equals("sort_by_date"))
                movies = iemdbSystem.getMoviesOrderByReleaseDate();
        }
        return movies;
    }
}
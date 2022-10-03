package com.ie.CA6.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    private String email;
    private String password;
    private String nickname;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Watchlist", joinColumns = @JoinColumn(name = "USER_ID"),inverseJoinColumns = @JoinColumn(name = "MOVIE_ID"))
    private Set<Movie> watch_list;

    private String birthDate;

    public String getName() { return name;}

    public String getEmail() {return email;}

    public String getNickname() {return nickname;}

    public String getPassword(){ return password;}
    public Set<Movie> getWatchList() {return this.watch_list;}

    public void addMovieToWatchList(Movie movie){
        watch_list.add(movie);
    }
    public void removeMovieFromWatchList(Movie movie) {
        watch_list.remove(movie);
    }
}
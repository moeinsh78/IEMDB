package com.ie.CA8.Entity;

import javax.persistence.*;
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

    public void setName(String name) { this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) { this.email = email; }

    public String getNickname() {return nickname;}

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPassword(){ return password;}

    public void setPassword(String password) { this.password = password; }

    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public Set<Movie> getWatchList() {return this.watch_list;}

    public void addMovieToWatchList(Movie movie){
        watch_list.add(movie);
    }
    public void removeMovieFromWatchList(Movie movie) {
        watch_list.remove(movie);
    }
}
package com.ie.CA6.Entity;

import javax.persistence.*;

@Entity
@Table(name = "rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int score;
    private String userId;
    private int movieId;

    public float getScore() {
        return score;
    }

    public void setScore(int _score) {
        score = _score;
    }

    public void setMovieId(int movie_id) {
        movieId = movie_id;
    }

    public void setUserId(String user_id) {
        userId = user_id;
    }
}

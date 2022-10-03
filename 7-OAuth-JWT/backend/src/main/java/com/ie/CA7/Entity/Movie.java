package com.ie.CA7.Entity;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.util.*;
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    private int id;
    private String name;
    @Column(columnDefinition="MEDIUMTEXT")
    private String summary;
    private String releaseDate;
    private String director;

    @Column
    @ElementCollection(targetClass=String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<String> writers;
    @Column
    @ElementCollection(targetClass=String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<String> genres;
    private double imdbRate;
    private int duration;
    private int ageLimit;
    private String image;
    private String coverImage;
    private ArrayList<Integer> cast;

    @ManyToMany
    @JoinTable(name="movies_cast", joinColumns = @JoinColumn(name = "MOVIES_ID"), inverseJoinColumns = @JoinColumn(name = "ACTORS_ID"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private final Set<Actor> actors = new HashSet<>();

    private double recommendationScore;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Rate> ratings;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Comment> comments;


    public int getId() {return id;}

    public String getSummary() {return summary;}

    public String getImage () {return image;}

    public String getCoverImage() {return coverImage;}

    public String getReleaseDate() {return releaseDate;}

    public Set<String> getWriters() {return writers;}

    public List<Integer> getCast() {return cast;}

    public int getDuration() {return duration;}

    public double getImdbRate() {return imdbRate;}

    public int getAgeLimit() {return ageLimit;}

    public Set<Rate> getRatings() {return ratings;}

    public ArrayList<String> getCastNames() {
        ArrayList<String> cast_names = new ArrayList<>();
        for (Actor actor : actors) {
            cast_names.add(actor.getName());
        }
        return cast_names;
    }

    public Set<Comment> getComments() {return comments;}

    public void setActor(Actor actor) {actors.add(actor);}

    public String getName(){return name;}

    public String getDirector(){return director;}

    public Set<String> getGenres() {return genres;}

    public void addComment(Comment comment) {comments.add(comment);}

    public double getRecommendationScore() {return recommendationScore;}

    public double getRatingsAverage() {
        int rating_count = 0;
        double total_rating = 0;
        for (Rate rate : ratings) {
            rating_count += 1;
            total_rating += rate.getScore();
        }
        return total_rating / rating_count;
    }

    public void rate(Rate rate){ratings.add(rate);}

    public int getNumberOfSameGenre(Movie movie) {
        int numbers = 0;
        Set<String> m1_genres = movie.getGenres();
        for(String genre : genres){
            if(m1_genres.contains(genre))
                numbers += 1;
        }
        return numbers;
    }

    public int getGenreSimilarity(HashMap<Integer,Movie> watchlist){
        int similarity = 0;
        for (HashMap.Entry<Integer, Movie> watchlist_movie : watchlist.entrySet()) {
            similarity += getNumberOfSameGenre(watchlist_movie.getValue());
        }
        return similarity;
    }

    public void setRecommendationScore(HashMap<Integer,Movie> watchlist){
        recommendationScore = 3 * getGenreSimilarity(watchlist) + imdbRate + getRatingsAverage();
    }

    public Set<Actor> getActors() {return actors;}
}

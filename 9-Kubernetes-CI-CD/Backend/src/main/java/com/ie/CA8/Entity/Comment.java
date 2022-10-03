package com.ie.CA8.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userNickname;

    private String text;

    @JsonProperty("likes")
    private int likes = 0;

    @JsonProperty("dislikes")
    private int dislikes = 0;

    @JsonIgnore
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Vote> votes;

    public void setId(int _id){
        id = _id;
    }

    public void setText(String _text) {this.text = _text;}

    public void setUserNickname(String nickname){this.userNickname = nickname;}

    public int getId() {return id;}

    public String getText() {return text;}

    public String getUserNickname() {return userNickname;}

    public void addVote(Vote vote) {votes.add(vote);}

    public Set<Vote> getVotes() {return votes;}

    public void setLikes(int like) {likes+=like;}

    public void setDislikes(int dislike) {dislikes+=dislike;}
}

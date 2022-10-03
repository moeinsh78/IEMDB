package com.ie.CA8.Entity;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userId;
    private int vote;
    private int commentId;

    public void setUserId(String _id) {userId = _id;}
    public void setVote(int score) {vote = score;}
    public int getVote() {return vote;}
    public void setCommentId(int _id){commentId = _id;}
}

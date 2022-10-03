package com.ie.CA6.Repository;

import com.ie.CA6.Entity.Movie;
import com.ie.CA6.Entity.User;
import com.ie.CA6.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {
    Vote findByUserIdAndCommentId(String user_id, Integer comment_id);
}

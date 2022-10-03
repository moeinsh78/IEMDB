package com.ie.CA8.Repository;

import com.ie.CA8.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {
    Vote findByUserIdAndCommentId(String user_id, Integer comment_id);
}

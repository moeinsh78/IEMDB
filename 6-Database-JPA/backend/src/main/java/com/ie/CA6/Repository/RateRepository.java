package com.ie.CA6.Repository;

import com.ie.CA6.Entity.Rate;
import com.ie.CA6.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findByUserIdAndMovieId(String user_id, Integer movie_id);
}

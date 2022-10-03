package com.ie.CA8.Repository;

import com.ie.CA8.Entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findByUserIdAndMovieId(String user_id, Integer movie_id);
}

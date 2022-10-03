package com.ie.CA6.Repository;

import com.ie.CA6.Entity.Movie;
import com.ie.CA6.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}

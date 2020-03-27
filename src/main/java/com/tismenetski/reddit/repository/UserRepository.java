package com.tismenetski.reddit.repository;


import com.tismenetski.reddit.model.User;
import com.tismenetski.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}

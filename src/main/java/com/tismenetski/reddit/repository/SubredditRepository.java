package com.tismenetski.reddit.repository;

import com.tismenetski.reddit.model.Subreddit;
import com.tismenetski.reddit.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long> {
}

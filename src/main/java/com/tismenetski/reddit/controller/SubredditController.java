package com.tismenetski.reddit.controller;

import com.tismenetski.reddit.dto.SubredditDto;
import com.tismenetski.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubReddit(@RequestBody SubredditDto subredditDto)
    {
         return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits()
    {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }
}

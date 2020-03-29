package com.tismenetski.reddit.controller;

import com.tismenetski.reddit.dto.PostRequest;
import com.tismenetski.reddit.dto.PostResponse;
import com.tismenetski.reddit.dto.SubredditDto;
import com.tismenetski.reddit.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {


    private final PostService postService;

    //create post --> 201 returned after successful call
    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest)
    {
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //get post by id
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id)
    {
        return postService.getPost(id);
    }

    //read all posts under a subreddit
    @GetMapping("/")
    public List<PostResponse> getAllPosts()
    {
        return postService.getAllPosts();
    }

    @GetMapping("/by-subreddit/{id}")
    public List<PostResponse> getPostsBySubreddit(Long id)
    {
        return postService.getPostsBySubreddit(id);
    }

    @GetMapping("/by-user/{name}")
    public List<PostResponse> getPostsByUsername(String username)
    {
        return postService.getPostsByUsername(username);
    }

}

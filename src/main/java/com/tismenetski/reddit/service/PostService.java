package com.tismenetski.reddit.service;

import com.tismenetski.reddit.dto.PostRequest;
import com.tismenetski.reddit.dto.PostResponse;
import com.tismenetski.reddit.exceptions.PostNotFoundException;
import com.tismenetski.reddit.exceptions.SubredditNotFoundException;
import com.tismenetski.reddit.mapper.PostMapper;
import com.tismenetski.reddit.model.Post;
import com.tismenetski.reddit.model.Subreddit;
import com.tismenetski.reddit.model.User;
import com.tismenetski.reddit.repository.PostRepository;
import com.tismenetski.reddit.repository.SubredditRepository;
import com.tismenetski.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;


    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }



    //saving the post. we recieve PostRequest object ,
    // then we search the corresponding subreddit to assign the post to ,
    // then finally we use the postMapper to map the postrequest to a post object to be saved in the database
    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }


    //Recieve id , Create new post object by obtaining it from the  postRepository by id , then returning it by the postMapper
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow(()-> new PostNotFoundException("There is no post with id: "+ id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId)
    {
        Subreddit subreddit = subredditRepository.findById(subredditId).orElseThrow(() -> new SubredditNotFoundException("No subreddit found with ID - " + subredditId));
        List<Post> posts  = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }


    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("No user with username: "+ username));
        List<Post> posts = postRepository.findByUser(user);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }











    /*
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }



    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }   */
}

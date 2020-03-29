package com.tismenetski.reddit.mapper;


import com.tismenetski.reddit.dto.PostRequest;
import com.tismenetski.reddit.dto.PostResponse;
import com.tismenetski.reddit.model.Post;
import com.tismenetski.reddit.model.Subreddit;
import com.tismenetski.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate",expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit",source = "subreddit")
    @Mapping(target = "user",source = "user")
    @Mapping(target = "description",source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id",source = "postId")
    @Mapping(target = "postName",source = "postName")
    @Mapping(target = "description",source = "description")
    @Mapping(target = "url",source = "url")
    @Mapping(target = "subredditName",source = "subreddit.name")
    @Mapping(target = "userName",source = "user.username")
    PostResponse mapToDto(Post post);

}

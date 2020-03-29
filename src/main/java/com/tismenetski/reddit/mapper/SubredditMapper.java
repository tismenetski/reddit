package com.tismenetski.reddit.mapper;

import com.tismenetski.reddit.dto.SubredditDto;
import com.tismenetski.reddit.model.Post;
import com.tismenetski.reddit.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
//Had to change the lombok build to 1.18.4 since 1.18.8 didn't work
@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts",expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);
    default Integer mapPosts(List<Post> numberOfPosts) {return  numberOfPosts.size();}

    @InheritInverseConfiguration
    @Mapping(target = "posts",ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);

}

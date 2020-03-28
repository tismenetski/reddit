package com.tismenetski.reddit.service;


import com.tismenetski.reddit.dto.SubredditDto;
import com.tismenetski.reddit.model.Subreddit;
import com.tismenetski.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto)
    {
      Subreddit save =  subredditRepository.save(mapSubredditDto(subredditDto));
      subredditDto.setId(save.getId());
      return subredditDto;
    }

    @Transactional
    public Subreddit mapSubredditDto(SubredditDto subredditDto) {

        return Subreddit.builder()
                .name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll()
    {
       return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SubredditDto mapToDto(Subreddit subreddit)
    {
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();

    }
}

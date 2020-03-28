package com.tismenetski.reddit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDto {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;


}

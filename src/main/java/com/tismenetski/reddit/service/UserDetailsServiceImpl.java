package com.tismenetski.reddit.service;

import com.tismenetski.reddit.model.User;
import com.tismenetski.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
    {
        Optional<User> userOptional = userRepository.findByUsername(username); //Create an optional to get returned data from the user repository
        User user = userOptional.orElseThrow(()->new UsernameNotFoundException("No user found with username: " + username)); //assign optional to object user ,if null throw exception (UsernameNotFoundException spring built in exception)

        //return clear authentication
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities("USER"));

    }


    //Granting authority to a role called user
    private Collection<? extends GrantedAuthority> getAuthorities(String role)
    {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}

package com.tismenetski.reddit.service;

import com.tismenetski.reddit.dto.AuthenticationResponse;
import com.tismenetski.reddit.dto.LoginRequest;
import com.tismenetski.reddit.dto.RegisterRequest;
import com.tismenetski.reddit.exceptions.SpringRedditException;
import com.tismenetski.reddit.model.NotificationMail;
import com.tismenetski.reddit.model.User;
import com.tismenetski.reddit.model.VerificationToken;
import com.tismenetski.reddit.repository.UserRepository;
import com.tismenetski.reddit.repository.VerificationTokenRepository;
import com.tismenetski.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest)
    {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationMail("Please Activate your account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit, " +
                         "please click on the below url to activate your account : "+
                "http://localhost:8080/api/auth/accountVerification/"+ token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    //verifies the user account by creating an optional object , assigning it the token found in the verificationTokenRepository.
    //if found call fetchUserAndEnable with the optional ,or else throw exception
    public void verifyAccount(String token) {

       Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
       verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
       fetchUserAndEnable(verificationToken.get());
    }

    //receives verificationToken , extracts username ,creates user object and enables it ,finally saves in the repository.
    //transactional since we making changes in the database
    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken)
    {
        @NotBlank(message = "Username is required") String username = verificationToken.getUser().getUsername();
       User user = userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("User not found with name "  +username));

        user.setEnabled(true);
        userRepository.save(user);
    }

    // gets a login request(username , password) ,
    // creates Authentication using the given details,
    // then sets the authentication in the securityContextHolder
    // afterwards generates a new token using the jwtProvider class which returns a token string
    // returns AuthenticationResponse containing the token and the user
    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getUsername());
    }

    //principal object get the current principal out of SecurityContextHolder , then we return the user for the userRepository if found
    public User getCurrentUser()
    {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();


        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("User name not found - "+principal.getUsername()));

    }
}

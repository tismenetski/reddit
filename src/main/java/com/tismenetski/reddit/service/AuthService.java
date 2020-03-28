package com.tismenetski.reddit.service;

import com.tismenetski.reddit.dto.RegisterRequest;
import com.tismenetski.reddit.model.NotificationMail;
import com.tismenetski.reddit.model.User;
import com.tismenetski.reddit.model.VerificationToken;
import com.tismenetski.reddit.repository.UserRepository;
import com.tismenetski.reddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

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
}

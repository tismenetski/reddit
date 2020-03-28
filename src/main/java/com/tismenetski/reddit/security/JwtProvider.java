package com.tismenetski.reddit.security;


import com.tismenetski.reddit.exceptions.SpringRedditException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct //this method will be executed once the bean has been constructed automatically
    // (we use the springblog.jks file that located in the resource folder
    public void init()
    {
        try
        {
            keyStore = KeyStore.getInstance("JKS"); // create instance of JKS
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks"); //store file contents in  resourceAsStream
            keyStore.load(resourceAsStream,"secret".toCharArray());
        }catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e)
        {
            throw new SpringRedditException("Exception occurred while loading keystore");
        }


    }

    //Responsible for generating a token using keyStore
    public  String generateToken(Authentication authentication)
    {
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }


    //Get a key from keystore , on error throw exception
    private PrivateKey getPrivateKey()
    {
        try
        {
            return (PrivateKey) keyStore.getKey("springblog","secret".toCharArray());
        }catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e)
        {
            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
        }

    }
}

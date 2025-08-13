package com.example.BookReview.service;

import com.example.BookReview.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public Authentication doAuthenticate(String email, String password) throws GlobalException {
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new GlobalException("Invalid Username And Password", HttpStatus.BAD_REQUEST);
        }
    }
}

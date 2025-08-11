package com.example.BookReview.controller;

import com.example.BookReview.dto.request.LoginRequest;
import com.example.BookReview.dto.response.JwtResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.security.JwtHelper;
import com.example.BookReview.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) throws GlobalException {
        Authentication authentication = doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtResponse jwtResponse = jwtHelper.generateToken(userDetails);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private Authentication doAuthenticate(String email, String password) throws GlobalException {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password);
        try {
            return authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new GlobalException("Invalid Username or Password", HttpStatus.BAD_REQUEST);
        }
    }

}

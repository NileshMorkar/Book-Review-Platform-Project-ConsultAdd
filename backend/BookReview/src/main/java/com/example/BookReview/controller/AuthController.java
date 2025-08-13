package com.example.BookReview.controller;

import com.example.BookReview.dto.request.LoginRequest;
import com.example.BookReview.dto.response.JwtResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.security.JwtHelper;
import com.example.BookReview.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) throws GlobalException {
        Authentication authentication = authService.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtResponse jwtResponse = jwtHelper.generateToken(userDetails);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

}

package com.example.BookReview.controller;

import com.example.BookReview.dto.request.UserRequest;
import com.example.BookReview.dto.response.UserResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) throws GlobalException {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }


}

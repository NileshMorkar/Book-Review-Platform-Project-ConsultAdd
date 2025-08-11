package com.example.BookReview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/test")
    public ResponseEntity<String> testMethod() {
        return ResponseEntity.ok("Hello Public Route!");
    }

}

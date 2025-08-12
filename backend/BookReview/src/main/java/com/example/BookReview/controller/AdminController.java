package com.example.BookReview.controller;

import com.example.BookReview.dto.request.AdminRequest;
import com.example.BookReview.dto.response.AdminResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest adminRequest) throws GlobalException {
        return ResponseEntity.ok(adminService.createAdmin(adminRequest));
    }

}

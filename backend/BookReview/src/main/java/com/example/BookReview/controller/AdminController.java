package com.example.BookReview.controller;

import com.example.BookReview.dto.request.AdminRequest;
import com.example.BookReview.dto.response.AdminResponse;
import com.example.BookReview.dto.response.ApiResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.AdminService;
import com.example.BookReview.service.auth.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest adminRequest) throws GlobalException {
        return ResponseEntity.ok(adminService.createAdmin(adminRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<AdminResponse> getMyDetails(@AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {
        int userId = (int) userDetails.getId();

        return ResponseEntity.ok(adminService.getMyDetails(userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadProfileImage(@RequestParam("profileImage") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {
        int adminId = (int) userDetails.getId();

        adminService.uploadProfileImage(adminId, file);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("Profile image uploaded successfully")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }


}

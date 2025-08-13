package com.example.BookReview.controller;

import com.example.BookReview.dto.request.UserRequest;
import com.example.BookReview.dto.response.ApiResponse;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.dto.response.UserResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.UserService;
import com.example.BookReview.service.auth.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) throws GlobalException {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyDetails(@AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {
        long userId = userDetails.getId();

        return ResponseEntity.ok(userService.getMyDetails(userId));
    }


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadProfileImage(@RequestParam("profileImage") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {
        long userId = userDetails.getId();

        userService.uploadProfileImage(userId, file);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("Profile image uploaded successfully")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<UserResponse>> getAllUsers(
            @RequestParam(name = "pageNum", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) throws GlobalException {
        pageNumber--;
        return ResponseEntity.ok(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir));
    }
}

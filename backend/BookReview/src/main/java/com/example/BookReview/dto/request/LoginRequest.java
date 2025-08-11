package com.example.BookReview.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email Is Not Valid")
    private String email;
    private String password;
}

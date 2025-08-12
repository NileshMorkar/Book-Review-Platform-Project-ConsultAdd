package com.example.BookReview.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {

    @NotEmpty(message = "Name is Required Field")
    private String name;

    @Email(message = "Email Is Incorrect")
    private String email;

    @Length(min = 6, message = "Password must be greater than 6 characters.")
    private String password;


}

package com.example.BookReview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "admins")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Name is Required Field")
    private String name;

    @Email(message = "Email Is Incorrect")
    @Column(unique = true)
    private String email;

    @Length(min = 6, message = "Password must be greater than 6 characters.")
    private String password;

}

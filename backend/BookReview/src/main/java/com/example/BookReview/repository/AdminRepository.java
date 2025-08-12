package com.example.BookReview.repository;

import com.example.BookReview.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    public Optional<Admin> findByEmail(String userEmail);
}

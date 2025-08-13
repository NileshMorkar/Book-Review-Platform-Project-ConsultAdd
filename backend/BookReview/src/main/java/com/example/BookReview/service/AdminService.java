package com.example.BookReview.service;

import com.example.BookReview.dto.request.AdminRequest;
import com.example.BookReview.dto.response.AdminResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.Admin;
import com.example.BookReview.repository.AdminRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j2
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminResponse createAdmin(AdminRequest adminRequest) throws GlobalException {

        try {
            adminRequest.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
            Admin admin = modelMapper.map(adminRequest, Admin.class);

            adminRepository.save(admin);

            return modelMapper.map(admin, AdminResponse.class);
        } catch (Exception e) {
            log.info("Admin Registration Failed - {}", e.getMessage());
            throw new GlobalException("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void uploadProfileImage(int adminId, MultipartFile file) throws GlobalException {

        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new GlobalException("User Is Already Present In Database, Please LogIn", HttpStatus.FOUND)
        );

        try {
            byte[] imageBytes = file.getBytes();
            admin.setProfileImage(imageBytes);

            adminRepository.save(admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public AdminResponse getMyDetails(int adminId) throws GlobalException {

        try {
            return modelMapper.map(adminRepository.findById(adminId).orElse(null), AdminResponse.class);
        } catch (Exception e) {
            throw new GlobalException(String.format("Getting User Details Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

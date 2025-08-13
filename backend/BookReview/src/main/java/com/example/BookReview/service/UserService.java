package com.example.BookReview.service;

import com.example.BookReview.dto.request.UserRequest;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.dto.response.UserResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.User;
import com.example.BookReview.repository.UserRepository;
import com.example.BookReview.util.Helper;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public PageableResponse<UserResponse> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) throws GlobalException {
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
            Page<User> page = userRepository.findAll(pageRequest);

            return Helper.getPageableResponse(page, UserResponse.class);
        } catch (Exception e) {
            log.error("User Exception - {}", e.getMessage());
            throw new GlobalException(String.format("User Exception - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public UserResponse createUser(UserRequest userRequest) throws GlobalException {

        try {
            Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
            if (userOptional.isPresent())
                throw new GlobalException("User Is Already Present In Database, Please LogIn", HttpStatus.FOUND);

            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            User user = modelMapper.map(userRequest, User.class);

            userRepository.save(user);
            return modelMapper.map(user, UserResponse.class);
        } catch (Exception e) {
            throw new GlobalException(String.format("User Registration Failed - %s", e.getMessage()), HttpStatus.FOUND);
        }
    }

    public void uploadProfileImage(long userId, MultipartFile file) throws GlobalException {

        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Is Already Present In Database, Please LogIn", HttpStatus.FOUND));

        try {
            byte[] imageBytes = file.getBytes();
            user.setProfileImage(imageBytes);

            userRepository.save(user);
        } catch (Exception e) {
            throw new GlobalException(String.format("Profile Image Upload Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public UserResponse getMyDetails(long userId) throws GlobalException {

        try {
            return modelMapper.map(userRepository.findById(userId).orElse(null), UserResponse.class);
        } catch (Exception e) {
            throw new GlobalException(String.format("Getting User Details Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

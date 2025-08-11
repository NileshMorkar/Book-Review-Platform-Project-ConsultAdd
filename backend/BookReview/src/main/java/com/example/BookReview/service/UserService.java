package com.example.BookReview.service;

import com.example.BookReview.dto.request.UserRequest;
import com.example.BookReview.dto.response.UserResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.User;
import com.example.BookReview.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
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
}

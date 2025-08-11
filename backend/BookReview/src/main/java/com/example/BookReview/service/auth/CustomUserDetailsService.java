package com.example.BookReview.service.auth;

import com.example.BookReview.models.Admin;
import com.example.BookReview.models.Role;
import com.example.BookReview.models.User;
import com.example.BookReview.repository.AdminRepository;
import com.example.BookReview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null) {
            return new CustomUserDetails(admin.getEmail(), admin.getPassword(), AuthorityUtils.createAuthorityList(Role.ROLE_ADMIN.name()));
        }

        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            return new CustomUserDetails(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(Role.ROLE_USER.name()));
        }

        throw new UsernameNotFoundException("User Not Found With Username: " + username);
    }
}
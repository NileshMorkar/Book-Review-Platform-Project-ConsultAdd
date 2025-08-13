package com.example.BookReview.controller;

import com.example.BookReview.exception.GlobalException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public void homePage(HttpServletResponse httpServletResponse) throws GlobalException {
        try {
            httpServletResponse.sendRedirect("/swagger-ui/index.html");
        } catch (Exception exception) {
            throw new GlobalException("Page Not Found Exception!", HttpStatus.NOT_FOUND);
        }
    }
}

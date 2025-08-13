package com.example.BookReview.util;

public class Constants {
    public static final String BEARER = "Bearer";

    public static final String JWT = "JWT";

    public static final String AUTHORIZATION = "Authorization";

    private static final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/students/signup",
            "/auth/login",
            "/public/**",
    };
}

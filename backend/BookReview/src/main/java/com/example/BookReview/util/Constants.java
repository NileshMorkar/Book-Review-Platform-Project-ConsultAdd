package com.example.BookReview.util;

public class Constants {
    public static final String BEARER = "Bearer";

    public static final String JWT = "JWT";

    public static final String AUTHORIZATION = "Authorization";

    public static final String[] PUBLIC_URLS = {
            "/",
            "/api/auth/login",
            "/api/users/signup",
            "/api/public/**",


            // Swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**"
    };
    public static final String[] ADMIN_URLS = {
            "/api/admins/**",
    };


}

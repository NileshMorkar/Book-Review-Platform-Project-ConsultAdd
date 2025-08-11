package com.example.BookReview.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("Swagger OpenAPI bean initialized.");
        return new OpenAPI();
    }


//    @Bean
//    public OpenAPI openAPI() {
//        String schemeName = "bearerScheme";
//        return new OpenAPI()
//                .addSecurityItem(
//                        new SecurityRequirement().addList(schemeName)
//                )
//                .components(
//                        new Components().addSecuritySchemes(
//                                schemeName
//                                , new SecurityScheme()
//                                        .name(schemeName)
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .bearerFormat(Constants.JWT)
//                                        .scheme(Constants.BEARER)
//                        )
//                )
//                .info(getInfo());
//    }


//    private Info getInfo() {
//
//        Contact contact = new Contact();
//        contact.setEmail("ndmorkar@gmail.com");
//        contact.setName("Nilesh Morkar");
//        contact.setUrl("https://www.google.com/");
//        return new Info()
//                .title("Electronic Store API")
//                .description("This Is Electronic Store Project API Developed By Nilesh Morkar")
//                .contact(contact)
//                .version("v0.0.1")
//                .summary("Welcome !!");
//
//    }
}
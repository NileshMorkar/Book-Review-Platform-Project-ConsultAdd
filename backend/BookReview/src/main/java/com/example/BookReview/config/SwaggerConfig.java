package com.example.BookReview.config;


import com.example.BookReview.util.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        String schemeName = "Bearer Token (JWT)";
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement().addList(schemeName)
                )
                .components(
                        new Components().addSecuritySchemes(
                                schemeName
                                , new SecurityScheme()
                                        .name(schemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat(Constants.JWT)
                                        .scheme(Constants.BEARER)
                        )
                )
                .info(getInfo());
    }


    private Info getInfo() {

        Contact contact = new Contact();
        contact.setName("Nilesh Morkar");
        contact.setEmail("nilesh.m@consultadd.com");

        return new Info()
                .title("Book Review API")
                .description("API documentation for Book Review system")
                .contact(contact)
                .version("1.0.0")
                .summary("Welcome !!!");

    }
}
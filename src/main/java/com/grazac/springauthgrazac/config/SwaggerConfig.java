package com.grazac.springauthgrazac.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


//http://localhost:9090/swagger-ui/index.html#/

@OpenAPIDefinition(
//        info = @Info(
//                contact = @Contact(
//                        name = "Grazac cass"
////                        email = "abayomiogunnusi@gmail.com",
////                        url = "https://github.com/drsimplegraffiti"
//                ),
////                description = "OpenApi documentation for my spring boot app",
////                title = "Java-Springboot Template",
//                version = "1.0",
//                license = @License(
//                        name = "Licence name",
//                        url = "https://github.com/drsimplegraffiti/Book-Social-Network/blob/main/LICENSE"
//
//                ),
//                termsOfService = "https://github.com/drsimplegraffiti/Book-Social-Network/blob/main/LICENSE"
//
//        ),
        servers = {@Server(
                description = "Local ENV",
                url = "http://localhost:9090" // dev internal testing
        ),
                @Server(
                        description ="Production ENV",
                        url = "https://daptee-58b9d8b2ab50.herokuapp.com"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }


)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
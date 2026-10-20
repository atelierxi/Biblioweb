package uis.edu.entornos.biblioweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "UUID")
public class BiblioWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiblioWebApplication.class, args);
    }
}

package com.Ecommerce.EcommerceApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggarConfig {

    @Bean
    public OpenAPI customOpenApi()  {
        SecurityScheme bearerScheme = new SecurityScheme(); 
                                            
        bearerScheme.type(SecurityScheme.Type.HTTP); 

        bearerScheme.scheme("bearer"); 
        bearerScheme.bearerFormat("JWT"); 
        bearerScheme.description("JWT Bearer Token"); 

       SecurityRequirement bearerRequirement = new SecurityRequirement(); 

       bearerRequirement.addList("Bearer Authentication");
       return new OpenAPI()
                        .components(new Components().addSecuritySchemes("Bearer Authentication", bearerScheme))
                        .addSecurityItem(bearerRequirement);  

        
    }
    
}

package com.Ecommerce.EcommerceApp.Security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Ecommerce.EcommerceApp.Models.User;
import com.Ecommerce.EcommerceApp.Security.Jwt.AuthEntryPointJwt;
import com.Ecommerce.EcommerceApp.Security.Jwt.AuthTokenFilter;

import jakarta.servlet.http.HttpServlet;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DataSource datasource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/auth/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .sameOrigin()))

                .csrf(csrf -> csrf.disable())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource); 
    }

    //  @Bean
    // public CommandLineRunner initData(UserDetailsService userDetailsService) {
    //     return args -> {
    //         JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
    //         UserDetails user1 = User.withUsername("user1")
    //                 .password(passwordEncoder().encode("password1"))
    //                 .roles("USER")
    //             UserDetails admin = User.withUsername("admin")
    //                 //.password(passwordEncoder().encode("adminPass"))
    //                 .password(passwordEncoder().encode("adminPass"))
    //                 .roles("ADMIN")
    //                 .build();

    //         JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
    //         userDetailsManager.createUser(user1);
    //         u       .build();
    //      serDetailsManager.createUser(admin);
    //     };
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) {
        return builder.getAuthenticationManager(); 
    }



}

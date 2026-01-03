package com.Ecommerce.EcommerceApp.Security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Ecommerce.EcommerceApp.Security.Jwt.AuthEntryPointJwt;
import com.Ecommerce.EcommerceApp.Security.Jwt.AuthTokenFilter;
import com.Ecommerce.EcommerceApp.Security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);

		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return authenticationProvider;

	}

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/api/auth/**")
			.permitAll()
			.requestMatchers("/v3/api-docs/**")
			.permitAll()
			.requestMatchers("api/v1/products/public/**")
			.permitAll()
			.requestMatchers("api/v1/public/**")
			.permitAll()
			.requestMatchers("/api/v1/admin/**")
			.permitAll()
			.requestMatchers("/images/**")
			.permitAll()
			.anyRequest()
			.authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
			// .headers(headers -> headers
			// .frameOptions(frameOptions -> frameOptions
			// .sameOrigin()))

			.csrf(csrf -> csrf.disable())
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		http.authenticationProvider(authenticationProvider());

		return http.build();

	}

	// @Bean
	// public UserDetailsService userDetailsService(DataSource dataSource) {
	// return new JdbcUserDetailsManager(dataSource);
	// }

	// @Bean
	// public CommandLineRunner initData(UserDetailsService userDetailsService) {
	// return args -> {
	// JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
	// UserDetails user1 = User.withUsername("user1")
	// .password(passwordEncoder().encode("password1"))
	// .roles("USER")
	// UserDetails admin = User.withUsername("admin")
	// //.password(passwordEncoder().encode("adminPass"))
	// .password(passwordEncoder().encode("adminPass"))
	// .roles("ADMIN")
	// .build();

	// JdbcUserDetailsManager userDetailsManager = new
	// JdbcUserDetailsManager(dataSource);
	// userDetailsManager.createUser(user1);
	// u .build();
	// serDetailsManager.createUser(admin);
	// };
	// }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) {
		return builder.getAuthenticationManager();
	}

	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web -> web.ignoring()
			.requestMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
					"/swagger-ui.html", "/webjars/**"));
	}

}

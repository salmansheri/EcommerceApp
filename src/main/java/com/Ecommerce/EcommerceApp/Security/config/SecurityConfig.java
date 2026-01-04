package com.Ecommerce.EcommerceApp.Security.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

import com.Ecommerce.EcommerceApp.Models.AppRole;
import com.Ecommerce.EcommerceApp.Models.Role;
import com.Ecommerce.EcommerceApp.Models.User;
import com.Ecommerce.EcommerceApp.Repositories.RoleRepository;
import com.Ecommerce.EcommerceApp.Repositories.UserRepository;
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
			.requestMatchers("/error/**")
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
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

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

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web -> web.ignoring()
			.requestMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
					"/swagger-ui.html", "/webjars/**"));
	}

	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseGet(() -> {
				Role newUserRole = new Role(AppRole.ROLE_USER);
				return roleRepository.save(newUserRole);

			});

			Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseGet(() -> {
				Role newSellerRole = new Role(AppRole.ROLE_SELLER);
				return roleRepository.save(newSellerRole);

			});

			Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> {
				Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
				return roleRepository.save(newAdminRole);

			});

			Set<Role> userRoles = Set.of(userRole);
			Set<Role> sellerRoles = Set.of(sellerRole);
			Set<Role> adminRoles = Set.of(adminRole);

			if (!userRepository.existsByUsername("user1")) {
				User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password"));
				userRepository.save(user1);
			}

			if (!userRepository.existsByUsername("admin")) {
				User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
				userRepository.save(admin);
			}

			userRepository.findByUsername("user1").ifPresent(user -> {
				System.out.println("Role user password " + user.getPassword());
				user.setRoles(userRoles);
				userRepository.save(user);
			});

			userRepository.findByUsername("admin").ifPresent(admin -> {
				System.out.println("Role admin password " + admin.getPassword());
				admin.setRoles(adminRoles);
				userRepository.save(admin);
			});

			userRepository.findByUsername("seller1").ifPresent(seller -> {
				System.out.println("Role seller password " + seller.getPassword());
				seller.setRoles(sellerRoles);
				userRepository.save(seller);
			});

		};
	}

}

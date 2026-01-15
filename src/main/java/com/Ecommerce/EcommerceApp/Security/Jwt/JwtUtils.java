package com.Ecommerce.EcommerceApp.Security.Jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.Ecommerce.EcommerceApp.Security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;

	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${spring.app.jwtCookieName}")
	private String jwtCookie;

	public String getjwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		log.debug("Authorization Header: {}", bearerToken);

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // Remove Bearer prefix
		}

		return null;

	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	public String generateTokenFromUsername(String username) {

		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key())
				.compact();
	}

	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}

	}

	public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
		String jwt = generateTokenFromUsername(userDetails.getUsername());

		ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
				.path("/api")
				.maxAge(24 * 60 * 60)
				.httpOnly(false)
				.build();

		return cookie;
	}

	public ResponseCookie removeJwtCookie() {

		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
				.path("/api")

				.build();

		return cookie;
	}

	public boolean validateJwtToken(String token) {
		try {
			System.out.println("------------Validate--------");
			log.debug("----------Validate-------");
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
			return true;

		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

}

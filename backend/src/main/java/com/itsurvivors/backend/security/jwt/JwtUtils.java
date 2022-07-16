package com.itsurvivors.backend.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import com.itsurvivors.backend.security.services.UserDetailsImpl;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	@Value("${itsurvivors.app.jwtSecret}")
	private String jwtSecret;
	@Value("${itsurvivors.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Signature JWT inválida: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Token JWT inválido: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("Token JWT expiró: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("Token JWT no soportado : {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("Cadena de pedida JWT vacía : {}", e.getMessage());
		}
		return false;
	}
}

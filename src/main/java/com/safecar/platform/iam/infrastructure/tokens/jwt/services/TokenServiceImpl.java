package com.safecar.platform.iam.infrastructure.tokens.jwt.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.Date;
import javax.crypto.SecretKey;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

import com.safecar.platform.iam.infrastructure.tokens.jwt.BearerTokenService;

@Service
public class TokenServiceImpl implements BearerTokenService {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    // Private methods

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String buildTokenWithDefaultParameters(String userId) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();
        return Jwts.builder()
                .subject(userId)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_TOKEN_PREFIX)) {
            return header.substring(BEARER_TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    public String generateToken(Authentication authentication) {
        return buildTokenWithDefaultParameters(authentication.getName());
    }

    @Override
    public String generateToken(String username) {
        return buildTokenWithDefaultParameters(username);
    }

    @Override
    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            LOGGER.info("Token is valid");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token is empty: {}", e.getMessage());
        }
        return false;
    }
}
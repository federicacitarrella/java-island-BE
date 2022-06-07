package com.bankIsland.account.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.app.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public int getAccountOwnerIdFromJwtToken(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        return (int) claims.get("userId");
    }

}

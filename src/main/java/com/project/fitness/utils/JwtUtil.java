package com.project.fitness.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private String jwtSecret = "q8J7x2Vn4Lm9Pz3Rk6Yw1Bc5Hd8Tf0Xs2Qa7Ui4Po6Ne3Wr9";
    private int jwtExpirationMs = 172800000;

    //Issue token
    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .subject(userId)
                //.claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .claim("roles", List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    //Generate token from BASE64 secret key
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Retrieving jwt from header
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            //Skipping first 7 characters from "Bearer <TOKEN>" to get <TOKEN>.
            return bearerToken.substring(7);
        }

        return null;
    }

    //Validating token
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    //Retrieve username from token
    public String getUserIdFromToken(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwt).getPayload().getSubject();
    }

    //Retrieve claims(roles) from token
    public Claims getAllClaims(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwt).getPayload();
    }
}

package com.example.autoannouncement.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "mysecretkey";

    public static SecretKey generateSecretKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);

            byte[] truncatedKey = new byte[32];
            System.arraycopy(keyBytes, 0, truncatedKey, 0, truncatedKey.length);

            return new SecretKeySpec(truncatedKey, SignatureAlgorithm.HS512.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при создании секретного ключа", e);
        }
    }

    public boolean validateToken(String token) {
        return getClaims(token) != null && !getClaims(token).getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String username = claims.get("username").toString();
        List<String> roles = (List<String>) claims.get("role");
        List<GrantedAuthority> userRoles = new ArrayList<>();
        for (String role : roles) {
            userRoles.add(new SimpleGrantedAuthority(role));
        }

        UserDetails userDetails = new User(username, "", userRoles);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims getClaims(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }
}

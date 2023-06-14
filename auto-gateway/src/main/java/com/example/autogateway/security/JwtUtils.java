package com.example.autogateway.security;

import com.example.autogateway.model.UserImplUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import java.util.HashMap;

@Component
public class JwtUtils {

    private static final String SECRET_KEY = "mysecretkey";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public static SecretKey generateSecretKey() {
        try {
            // Преобразуем секретный ключ в байтовый массив
            byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

            // Вычисляем хэш с использованием SHA-256
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);

            // Обрезаем ключ до нужной длины
            byte[] truncatedKey = new byte[32];
            System.arraycopy(keyBytes, 0, truncatedKey, 0, truncatedKey.length);

            // Создаем объект SecretKeySpec с использованием обрезанного ключа и алгоритма HS512
            return new SecretKeySpec(truncatedKey, SignatureAlgorithm.HS512.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при создании секретного ключа", e);
        }
    }

    public String generateToken(UserImplUserDetails user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhone());
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(generateSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        return getClaims(token) != null && !getClaims(token).getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        String username = getClaims(token)
                .getSubject();

        UserDetails userDetails = new User(username, "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String extractUsername(String authToken) {
        Claims claims = getClaims(authToken);
        return claims
                .get("email", String.class);
    }

    public Claims getClaims(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

//    @Value("${secret.key}")
//    private String secret;
//    private static final long EXPIRATION_TIME = 86400000; // 24 hours
//
//    public String generateToken(String userUUID, String role) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
//
//        Claims claims = Jwts.claims()
//                .setSubject(userUUID);
//        claims.put("role", role);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
//            return true;
//        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
//            return false;
//        }
//    }
}

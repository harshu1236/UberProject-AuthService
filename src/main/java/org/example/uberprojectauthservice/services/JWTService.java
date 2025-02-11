package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService implements CommandLineRunner {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.SECRET}")
    private String SECRET;

    public String generateToken (Map<String,Object> payload, String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);

        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .subject(username)
                .signWith(secretKey)
                .compact();
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("email","admin@mail.com");
        map.put("phone","9874674999");
        String token = generateToken(map,"admin");
        System.out.println(token);
    }
}

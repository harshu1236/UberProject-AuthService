package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
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
import java.util.function.Function;

@Service
public class JWTService implements CommandLineRunner {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.SECRET}")
    private String SECRET;

    // Generates a JWT token with payload and username
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

    // Extracts a specific JWT default key-value pair from the JWT Claim/payLoad
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    // Extracts a specific custom key-value pair from the Claim/payLoad
    public Object extractPayLoad(String token, String payLoadKey){
        final Claims claims = extractAllClaims(token);
        return (Object) claims.get(payLoadKey);
    }

    // Extracts all claims from the JWT token
    public Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(getSignKey())
                .build();
        Jws<Claims> claims = parser.parseSignedClaims(token);  // New method to parse claims securely

        return claims.getPayload();   // Use getPayLoad() instead of getBody()
    }

    // Retrieves the signing key for token verification
    public SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Extracts the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Extracts the username (subject) from the JWT token
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Checks if the JWT token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validates the token by checking the email and expiration status
    public Boolean validateToken(String token,String email){
        final String userEmailFetchedFromToken = extractClaim(token, Claims::getSubject);
        return userEmailFetchedFromToken.equals(email) && !isTokenExpired(token);
    }


    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("email","admin@mail.com");
        map.put("phone","9874674999");
        String token = generateToken(map,"admin@mail.com");
        System.out.println(token);

        System.out.println(extractUsername(token));
        System.out.println(extractExpiration(token));
        System.out.println(validateToken(token,"admin@mail.com"));
        System.out.println(extractPayLoad(token,"phone"));
    }
}

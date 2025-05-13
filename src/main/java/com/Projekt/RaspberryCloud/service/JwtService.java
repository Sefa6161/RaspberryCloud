package com.Projekt.RaspberryCloud.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // This key is visible on GitHub => on production use a new SECRET_KEY
    private static final String SECRET_KEY = "174c765c688c39a61dc97fedaa75328ce4c47e285a45ea4ea991e7029bc4454509627adaf0eadcdf14d8cc95c37b89e8cc5435e236c2d9264daa0a72b23ff809fd779fecbdb71bb178859d425d52f944be9bcda52cc640621115b4a84839cb70bb71411feb5db7500c427162cbcff7baea7487542468eec5898dcb832b4397cbe5f44413f471c4697871e48dcbe07ef5b053ce5d97fe5e0206cf47b2aed44acc62c2156cdabbf063ff750d1faa16cb583442f6259f5677baa4d42b643ebbd9b0a7a98e7475364fa275c55dcb7a59b3e665fdc4b218b6c390cce8fdac087ab954ce2272795dfee3e51a8af7dc5239fb855fc4e6a05913bebfb2c3d63522a66ab2";

    public JwtService() {
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userID", Integer.class));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

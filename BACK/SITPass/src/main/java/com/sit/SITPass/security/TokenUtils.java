package com.sit.SITPass.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Value("randomString")
    private String secret;

    @Value("3600") //3600 sec = 1h
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = this.getClaimsFromToken(token);

            username = claims.getSubject();

        } catch (Exception e) {
            username = null;
        }
        return username;
    }


    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {


            // Check if the token is null or empty
            if (token == null || token.isEmpty()) {

                return null;
            }

            // Check if the token is properly signed
            if (!Jwts.parser().isSigned(token)) {

                return null;
            }

            // Parse the token and extract claims
            claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

        } catch (MalformedJwtException e) {
            System.out.println("MalformedJwtException in getClaimsFromToken: " + e.getMessage());
            claims = null;
        } catch (ExpiredJwtException e) {
            System.out.println("ExpiredJwtException in getClaimsFromToken: " + e.getMessage());
            claims = null;
        } catch (UnsupportedJwtException e) {
            System.out.println("UnsupportedJwtException in getClaimsFromToken: " + e.getMessage());
            claims = null;
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException in getClaimsFromToken: " + e.getMessage());
            claims = null;
        } catch (Exception e) {
            System.out.println("Exception in getClaimsFromToken: " + e.getMessage());
            claims = null;
        }
        return claims;
    }



    public Date getExpirationDateFromToken(String token) {
        Date expirationDate;
        try {
            final Claims claims = this.getClaimsFromToken(token); // username izvlacimo iz expiration time polja unutar payload tokena
            expirationDate = claims.getExpiration();
        } catch (Exception e) {
            expirationDate = null;
        }
        return expirationDate;
    }

    /*
     * Provera da li je token istekao tj da li nije prsvto expiration momenat*/
    private boolean isTokenExpired(String token) {
        final Date expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    /*Provera validnosti tokena: period vazenja i provera username-a korisnika*/
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /*Generisanje tokena za korisnika - postavljanje svih potrebnih informacija,
     * kao sto je rola korisnika.*/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().toArray()[0]);
        claims.put("created", new Date(System.currentTimeMillis()));
        return Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

}

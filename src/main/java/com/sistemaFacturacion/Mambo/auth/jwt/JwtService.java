package com.sistemaFacturacion.Mambo.auth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.sistemaFacturacion.Mambo.entity.model.Usuario;
import com.sistemaFacturacion.Mambo.entity.model.cliente;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getToken(Usuario user) {
        UserDetails userDetails = buildUserDetails(user);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRol() != null ? user.getRol().getNombre() : "ADMIN"); 
        extraClaims.put("nombre", user.getNombre() != null ? user.getNombre() : user.getNumeroDocumento()); 
        return generateToken(extraClaims, userDetails);
    }

    public String getTokenCliente(cliente cliente) {
        UserDetails userDetails = buildClienteUserDetails(cliente);
        
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", cliente.getRol() != null ? cliente.getRol().getNombre() : "CLIENTE");
        extraClaims.put("nombre", cliente.getNombre() != null ? cliente.getNombre() : cliente.getNumeroDocumento());
        
        return generateToken(extraClaims, userDetails);
    }

    private UserDetails buildUserDetails(Usuario user) {
        String roleName = user.getRol() != null ? user.getRol().getNombre() : "ADMIN";
        return new User(
            user.getNumeroDocumento(),
            user.getContra() != null ? user.getContra() : "",
            user.isEnabled(),
            true, true, true,
            List.of(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }

    private UserDetails buildClienteUserDetails(cliente cliente) {
        String roleName = cliente.getRol() != null ? cliente.getRol().getNombre() : "CLIENTE";
        return new User(
            cliente.getNumeroDocumento(),
            cliente.getContra() != null ? cliente.getContra() : "",
            cliente.isEnabled(),
            true, true, true,
            List.of(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = getAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}

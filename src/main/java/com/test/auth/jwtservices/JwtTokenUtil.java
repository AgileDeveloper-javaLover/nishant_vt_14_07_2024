package com.test.auth.jwtservices;

import com.test.auth.modal.RolePermission;
import com.test.auth.modal.UserRole;
import com.test.auth.modal.Users;
import com.test.auth.repositories.RolePermissionRepository;
import com.test.auth.repositories.UserRepository;
import com.test.auth.repositories.UserRoleRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY =24 * 60 * 60;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;


    @Value("${jwt.publicKey}")
    private String jwtPublicKey;

    @Value("${jwt.privateKey}")
    private String jwtPrivateKey;

    public Boolean isValidToken(String token) {
        try {
            getClaims(token);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info("InValid TOKEN, Exception Occurred");
            return Boolean.FALSE;
        }
    }

    public String generateJwtToken(UserDetails userDetails) {
        Users usersEntity=usersRepository.findByEmail(userDetails.getUsername()).orElse(null);
        // Map<String, Set<String>> rolesMap = new HashMap<>();
        Set<Long> roleIdSet=new HashSet();
        Set<String> permissionSet=new HashSet();

        List<UserRole> userRoles=userRoleRepository.findByUserId(usersEntity.getId());
        if(usersEntity!=null && userRoles!=null) {
            for(UserRole userRole:userRoles)
                roleIdSet.add(userRole.getRole().getId());
        }

        List<RolePermission> rolePermissions=rolePermissionRepository.findAllByRoleIdIn(roleIdSet);
        if(rolePermissions!=null){
            for (RolePermission rolePermission : rolePermissions) {
                permissionSet.add(rolePermission.getPermission().getName());
            }
        }
        try {
            // rolesMap.put("permissions", permissionSet);
            return Jwts.builder()
                    .setIssuer("authtest")
                    .setSubject(userDetails.getUsername())
                    .setExpiration(Date.from(Instant.now().plusSeconds(JWT_TOKEN_VALIDITY)))
                    .setNotBefore(Date.from(Instant.now()))
                    .setIssuedAt(Date.from(Instant.now()))
                    .claim("type", "Bearer")
                    .claim("permissions", permissionSet)
                    .claim("preferred_username", usersEntity.getEmail())
                    .setId(UUID.randomUUID().toString()).signWith(getPrivateKey()).compact();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while generating jwt token", e);
            throw new SecurityException("Something went wrong!!",e);
        }
    }

    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            log.error("Token is expired", e);
            throw new SecurityException("Token is expired", e);
        } catch (SignatureException e){
            log.error("Token is signed with wrong signature", e);
            throw new SecurityException("Error parsing jwt token. Token is wrong");
        } catch (IllegalArgumentException | MalformedJwtException | UnsupportedJwtException e){
            log.error("Error parsing jwt token. Token is invalid", e);
            throw new SecurityException("Error parsing jwt token. Token is invalid", e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch(Exception e){
            log.error("cannot decode token", e);
            throw new RuntimeException(e);
        }
    }

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getMimeDecoder().decode(jwtPublicKey.getBytes(StandardCharsets.UTF_8));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] keyBytes = Base64.getMimeDecoder().decode(jwtPrivateKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(keySpec);
    }
}

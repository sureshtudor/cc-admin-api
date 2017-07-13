package com.cc.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
class TokenAuthenticationService {

    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    static long JWT_EXPIRATION;
    static String JWT_SECRET;
    static String CC_ROLE_PREFIX;

    @Value("${jwt.secret}")
    void setJwtSecret(String val) {
        JWT_SECRET = val;
    }

    @Value("${jwt.expiration}")
    void setJwtExpiration(long val) {
        JWT_EXPIRATION = val;
    }

    @Value("${ad.cc_role_prefix}")
    void setCCRolePrefix(String val) {
        CC_ROLE_PREFIX = val;
    }

    static void addAuthentication(HttpServletResponse res, Authentication auth) throws IOException {

        String JWT = Jwts.builder()
                .setSubject(auth.getName())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

        // attach auth token to response header.
//        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
//        res.addHeader("Access-Control-Allow-Origin", "*");

        // send details in the body.
        AuthenticatedUser user = toAuthenticatedUser(auth);
        user.setToken(JWT);
        res.getWriter().write(new ObjectMapper().writeValueAsString(user));
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return user != null ? new UsernamePasswordAuthenticationToken(
                    user, null, emptyList()) : null;
        }
        return null;
    }

    static AuthenticatedUser toAuthenticatedUser(Authentication auth) {
        AuthenticatedUser user = new AuthenticatedUser();
        user.setUsername(auth.getName());
        user.setFullname(getName(((LdapUserDetails) auth.getPrincipal()).getDn()));
        user.setRoles(auth.getAuthorities().stream()
                .filter(o -> o.getAuthority().startsWith(CC_ROLE_PREFIX))
                .map(o -> o.getAuthority()).collect(Collectors.toList()));
        return user;
    }

    static String getName(String dn) {
        return dn.split(",")[0].replace("CN=", "");
    }
}

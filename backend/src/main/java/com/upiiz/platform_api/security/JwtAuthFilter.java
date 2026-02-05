package com.upiiz.platform_api.security;

import com.upiiz.platform_api.repositories.UserRepository;
import com.upiiz.platform_api.services.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthFilter extends GenericFilter {
    private final JwtService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserRepository users){
        this.jwt = jwt; this.users = users;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        var r = (HttpServletRequest) req;
        var a = r.getHeader("Authorization");
        if (a != null && a.startsWith("Bearer ")) {
            var token = a.substring(7);
            try {
                var email = jwt.subject(token);
                var u = users.findByEmailInst(email).orElse(null);
                if (u != null && u.isActive()){
                    var auths = u.getRoles().stream()
                            .map(rr -> new SimpleGrantedAuthority("ROLE_" + rr.getName()))
                            .toList();
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(email, null, auths));
                }
            } catch (Exception ignored) {}
        }
        chain.doFilter(req, res);
    }
}

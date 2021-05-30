package fr.genielogiciel.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {

            final String requestTokenHeader = request.getHeader(JwtUtils.headerAuthorization);
            System.out.println("Auth : " + requestTokenHeader);

            if(request.getHeader(JwtUtils.headerAuthorization) != null && requestTokenHeader.startsWith(JwtUtils.headerAuthorizationPrefix)) {
                String requestJwt = requestTokenHeader.substring(7);
                JwtParser build = Jwts.parserBuilder().setSigningKey(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8)).build();
                Claims body = build.parseClaimsJws(requestJwt).getBody();

                String username = body.getSubject();
                System.out.println("Connected as " + username);


                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

}

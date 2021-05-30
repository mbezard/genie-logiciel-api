package fr.genielogiciel.security.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtAuthCredentialsFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthCredentialsFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .signWith(Keys.hmacShaKeyFor(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8)))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (1000 * 60 * 60 * 24 * 2)))//2 days
                .compact();

        response.addHeader(JwtUtils.headerAuthorization, JwtUtils.headerAuthorizationPrefix + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject object = new JSONObject();
        object.put("token", JwtUtils.headerAuthorizationPrefix + token);

        PrintWriter writer = response.getWriter();
        writer.write(object.toString());


    }
}

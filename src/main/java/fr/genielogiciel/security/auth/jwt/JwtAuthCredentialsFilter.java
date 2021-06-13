package fr.genielogiciel.security.auth.jwt;

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

public class JwtAuthCredentialsFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthCredentialsFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("attempt Authentification");
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
        String token = JwtUtils.generateToken(authResult.getName());

        response.addHeader(JwtUtils.headerAuthorization, JwtUtils.headerAuthorizationPrefix + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject object = new JSONObject();
        object.put("token", JwtUtils.headerAuthorizationPrefix + token);

        PrintWriter writer = response.getWriter();
        writer.write(object.toString());


    }
}

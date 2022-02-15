package at.qe.skeleton.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws JsonGenerationException, JsonMappingException, IOException, ServletException {
	if(request.getServletPath().equals("/login") || request.getServletPath().equals("/api/token/refresh"))
	    filterChain.doFilter(request, response);
	else {
	    String authHeader = request.getHeader("AUTHORIZATION");
	    if(authHeader != null && authHeader.startsWith("Bearer ")) {
		try {
                    String token = authHeader.substring("Bearer ".length());
                    Algorithm algo = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algo).build();
                    DecodedJWT decodedjwt = verifier.verify(token);
                    String username = decodedjwt.getSubject();
                    String[] roles = decodedjwt.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for(String role : roles)
                	authorities.add(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken autToken = 
                	    new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(autToken);
                    filterChain.doFilter(request, response);
		}
		catch(Exception ex) {
		    response.setHeader("error", ex.getMessage());
		    response.setStatus(403, "FORBIDDEN");
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", ex.getMessage());
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
		}
	    }
            else
                filterChain.doFilter(request, response);
	}
	    
	
    }

}

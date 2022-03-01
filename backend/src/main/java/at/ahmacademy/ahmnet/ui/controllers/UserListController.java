package at.ahmacademy.ahmnet.ui.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ahmacademy.ahmnet.dtos.UserDto;
import at.ahmacademy.ahmnet.dtos.UserMapper;
import at.ahmacademy.ahmnet.model.User;
import at.ahmacademy.ahmnet.model.UserRole;
import at.ahmacademy.ahmnet.services.UserService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class UserListController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        Collection<UserDto> dtos = UserMapper.mapToUserDto(userService.getAllUsers());
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }

    @GetMapping("/allTrainers")
    public ResponseEntity<?> getAllTrainers() {
        Collection<UserDto> dtos = UserMapper.mapToUserDto(userService.getAllTrainer());
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }

    @GetMapping("/allPlayers")
    public ResponseEntity<?> getAllPlayers() {
        Collection<UserDto> dtos = UserMapper.mapToUserDto(userService.getAllPlayer());
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dtos);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(String username) {
        UserDto dto = UserMapper.mapToUserDto(userService.loadUser(username));
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(dto);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(String password) {
        this.userService.changePassword(password);
	return ResponseEntity
	            .status(HttpStatus.OK)
	            .build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
        String authHeader = request.getHeader("AUTHORIZATION");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authHeader.substring("Bearer ".length());
                Algorithm algo = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algo).build();
                DecodedJWT decodedjwt = verifier.verify(refresh_token);
                String username = decodedjwt.getSubject();
                User user = this.userService.loadUser(username);

                // create new access-token
                String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getRoles().stream().map(UserRole::toString).collect(Collectors.toList()))
                    .sign(algo);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType("appication/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch(Exception ex) {
                response.setHeader("error", ex.getMessage());
                response.setStatus(403, "FORBIDDEN");
                Map<String, String> error = new HashMap<>();
                error.put("error_message", ex.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }

        else
            throw new RuntimeException("Refresh token is missing");
    }
    

}

package br.com.rufuziu.crud_users_and_auth.controller;

import br.com.rufuziu.crud_users_and_auth.dto.user.UserDTO;
import br.com.rufuziu.crud_users_and_auth.security.jwt.JwtUtils;
import br.com.rufuziu.crud_users_and_auth.services.EmailService;
import br.com.rufuziu.crud_users_and_auth.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.Base64;

@RestController
@RequestMapping("/api/")
@Validated
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    private String checkTokenIntegrity(String token)
            throws JsonProcessingException {
        jwtUtils.validateJwtToken(token);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);
        return jsonNode.get("sub").asText();
    }

    @PatchMapping("v1/users/active/{email}")
    public ResponseEntity<String> userActivate(@PathVariable String email) {
        if (userService.activateUserByEmail(email) > 0)
            return ResponseEntity.ok("User activated!");
        else return ResponseEntity.badRequest().build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("v1/users/create/user")
    public ResponseEntity<UserDTO> userCreate(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        try {
            String token;

            if (WebUtils.getCookie(request, "token") == null) {
                token = request.getHeader("Authorization");
                token = token.replaceAll("Bearer ", "");
            } else {
                token = WebUtils.getCookie(request, "token").getValue();
            }

            if (token != "") {
                String email = checkTokenIntegrity(token);
                UserDTO user = new UserDTO();
                user.setEmail("You cannot create a new user!");
                return ResponseEntity.badRequest()
                        .body(user);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("v1/users/read/user")
    public ResponseEntity<UserDTO> userRead(HttpServletRequest request) {
        try {
            String token;

            if (WebUtils.getCookie(request, "token") == null) {
                token = request.getHeader("Authorization");
                token = token.replaceAll("Bearer ", "");
            } else {
                token = WebUtils.getCookie(request, "token").getValue();
            }

            if (token != "") {
                String email = checkTokenIntegrity(token);
                UserDTO user = userService.readUser(email);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("v1/users/update/user")
    public ResponseEntity<UserDTO> userUpdate(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        try {
            String token;

            if (WebUtils.getCookie(request, "token") == null) {
                token = request.getHeader("Authorization");
                token = token.replaceAll("Bearer ", "");
            } else {
                token = WebUtils.getCookie(request, "token").getValue();
            }

            if (token != "") {
                String email = checkTokenIntegrity(token);
                userDto = userService.updateUser(userDto, email);
                return ResponseEntity.ok()
                        .body(userDto);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("v1/users/delete/user")
    public ResponseEntity<UserDTO> userDelete(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        try {
            String token;

            if (WebUtils.getCookie(request, "token") == null) {
                token = request.getHeader("Authorization");
                token = token.replaceAll("Bearer ", "");
            } else {
                token = WebUtils.getCookie(request, "token").getValue();
            }

            if (token != "") {
                String email = checkTokenIntegrity(token);
                UserDTO user = new UserDTO();
                user.setEmail("You cannot delete a user!");
                return ResponseEntity.badRequest()
                        .body(user);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

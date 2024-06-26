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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(UserController.class);

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
    public ResponseEntity<String> userActivate(HttpServletRequest request,
                                               @PathVariable String email) {
        log.info(request.getRemoteAddr().concat(" is trying to activate the e-mail"));
        if (userService.activateUserByEmail(email) > 0){
            log.info(request.getRemoteAddr().concat(" activated the e-mail"));
            return ResponseEntity.ok("User activated!");
        }
        else{
            log.error(request.getRemoteAddr().concat(" not activated the e-mail"));
            return ResponseEntity.badRequest().build();
        }
    }

    //Not able to create a user
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("v1/users/create/user")
    public ResponseEntity<UserDTO> userCreate(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        log.info(request.getRemoteAddr().concat(" is trying to create a user"));
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
        log.info(request.getRemoteAddr().concat(" is trying to read a user"));
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
                log.info(request.getRemoteAddr().concat(" will read a user"));
                return ResponseEntity.ok(user);
            } else {
                log.info(request.getRemoteAddr().concat(" with invalid token"));
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error(request.getRemoteAddr().concat(e.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("v1/users/update/user")
    public ResponseEntity<UserDTO> userUpdate(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        log.info(request.getRemoteAddr().concat(" is trying to update a user"));
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
                log.info(request.getRemoteAddr().concat(" updated the user"));
                return ResponseEntity.ok()
                        .body(userDto);
            } else {
                log.info(request.getRemoteAddr().concat(" with invalid token"));
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error(request.getRemoteAddr().concat(e.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    //Not able to delete a user
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("v1/users/delete/user")
    public ResponseEntity<UserDTO> userDelete(HttpServletRequest request,
                                              @RequestBody @Valid UserDTO userDto) {
        log.info(request.getRemoteAddr().concat(" is trying to delete a user"));
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
                log.info(request.getRemoteAddr().concat(" with invalid token"));
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            log.error(request.getRemoteAddr().concat(e.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }
}

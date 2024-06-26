package br.com.rufuziu.crud_users_and_auth.controller;

import br.com.rufuziu.crud_users_and_auth.dto.auth.AuthDTO;
import br.com.rufuziu.crud_users_and_auth.dto.user.UserDTO;
import br.com.rufuziu.crud_users_and_auth.dto.user.UserLoginDTO;
import br.com.rufuziu.crud_users_and_auth.security.dto.UserDetailsImpl;
import br.com.rufuziu.crud_users_and_auth.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/v1/auth")
    public ResponseEntity<UserLoginDTO> authenticateUser(HttpServletRequest request,
                                                         @Valid @RequestBody AuthDTO authDto) {
        log.info(request.getRemoteAddr().concat(" is trying to authenticate"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

        try {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());


            UserLoginDTO user = new UserLoginDTO(userDetails.getId(),userDetails.getEmail(),jwtCookie.getValue());
            log.info(request.getRemoteAddr().concat(" is successfully authenticated"));
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(user);
        }catch(Exception e){
            log.error(request.getRemoteAddr().concat(" is successfully authenticated"));
            return ResponseEntity.badRequest().build();
        }
    }

}

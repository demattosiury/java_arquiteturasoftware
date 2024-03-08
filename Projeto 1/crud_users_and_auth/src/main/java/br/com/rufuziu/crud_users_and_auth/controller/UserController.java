package br.com.demattosiury.crud_users_and_auth.controller;

import br.com.demattosiury.crud_users_and_auth.dto.UserDTO;
import br.com.demattosiury.crud_users_and_auth.services.EmailService;
import br.com.demattosiury.crud_users_and_auth.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Validated
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService,
                          EmailService emailService
    ) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("v1/users")
    public ResponseEntity<String> create(@RequestBody @Valid UserDTO userDto) {
        Boolean value = userService.createUser(userDto);
        if (value) {
            emailService.sendEmail(userDto.getEmail(), "ACTIVE YOUR ACCOUNT", "Click here!");
            return ResponseEntity.status(HttpStatus.CREATED).body("User created!");
        } else return
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create a new user!");
    }

}

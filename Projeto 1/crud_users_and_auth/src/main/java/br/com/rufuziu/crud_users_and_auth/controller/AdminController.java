package br.com.rufuziu.crud_users_and_auth.controller;

import br.com.rufuziu.crud_users_and_auth.dto.user.UserDTO;
import br.com.rufuziu.crud_users_and_auth.services.EmailService;
import br.com.rufuziu.crud_users_and_auth.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@Validated
public class AdminController {

    private final UserService userService;
    private final EmailService emailService;

    public AdminController(UserService userService,
                           EmailService emailService
    ) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("v1/admin/create/user")
    public ResponseEntity<String> adminCreate(@RequestBody @Valid UserDTO userDto) {
        Boolean value = userService.createUser(userDto);
        if (value) {
            emailService.sendEmail(userDto.getEmail(), "ACTIVE YOUR ACCOUNT", "<button>Click here!</button>");
            return ResponseEntity.status(HttpStatus.CREATED).body("User created!");
        } else return
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create a new user!");
    }
}

package br.com.rufuziu.crud_users_and_auth.controller;

import br.com.rufuziu.crud_users_and_auth.dto.user.UserDTO;
import br.com.rufuziu.crud_users_and_auth.services.EmailService;
import br.com.rufuziu.crud_users_and_auth.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService userService,
                           EmailService emailService
    ) {
        this.userService = userService;
        this.emailService = emailService;
    }


    @PostMapping("v1/admin/create/user")
    public ResponseEntity<UserDTO> adminCreate(HttpServletRequest request,
                                               @RequestBody @Valid UserDTO userDto) {
        log.info(request.getRemoteAddr().concat(" is sending request to create a user"));
        userDto = userService.createUser(userDto);
        if (userDto.getId() > 0) {
            log.info(request.getRemoteAddr().concat(" created a user"));
            //emailService.sendEmail(userDto.getEmail(), "ACTIVE YOUR ACCOUNT", "<button>Click here!</button>");
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } else {
            log.error(request.getRemoteAddr().concat(" failed to create a user"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userDto);
        }
    }
}

package br.com.rufuziu.crud_users_and_auth.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class UserNotFound extends RuntimeException {
        public UserNotFound(String message) {
            super(message);
        }
}

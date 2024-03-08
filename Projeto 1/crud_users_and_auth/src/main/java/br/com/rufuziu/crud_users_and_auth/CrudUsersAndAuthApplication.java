package br.com.demattosiury.crud_users_and_auth;

import br.com.demattosiury.crud_users_and_auth.services.EmailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class CrudUsersAndAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudUsersAndAuthApplication.class, args);
	}

}

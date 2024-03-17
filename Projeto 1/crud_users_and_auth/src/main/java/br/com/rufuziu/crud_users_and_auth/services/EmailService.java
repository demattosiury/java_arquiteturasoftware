package br.com.rufuziu.crud_users_and_auth.services;

import br.com.rufuziu.crud_users_and_auth.entity.FailedMail;
import br.com.rufuziu.crud_users_and_auth.repository.FailedMailRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final FailedMailRepository repository;

    public EmailService(JavaMailSender javaMailSender,
                        FailedMailRepository repository) {
        this.javaMailSender = javaMailSender;
        this.repository = repository;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            repository.save(new FailedMail(to,subject,body));
        }
    }
}

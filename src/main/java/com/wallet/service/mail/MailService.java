package com.wallet.service.mail;

import com.wallet.model.dto.mail.Mail;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(Mail mail) {
        try {
            mailSender.send(buildMailMessage(mail));
        } catch (Exception e) {
            log.error("Unable to send message for recipient {}", mail.getRecipient());
        }
    }

    private SimpleMailMessage buildMailMessage(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(mail.getRecipient());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());

        return message;
    }
}

package com.wallet.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wallet.model.dto.mail.Mail;
import com.wallet.service.mail.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = MailService.class)
public class MailServiceTest {

    private MailService mailService;

    @MockBean
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        mailService = new MailService(
            mailSender
        );
        ReflectionTestUtils.setField(mailService, "sender", "dummy-sender");
    }

    @Test
    void sendMail_test() {
        // given
        var mail = Mail.builder()
            .recipient("dummy")
            .subject("dummy")
            .message("dummy")
            .build();
        var message = new SimpleMailMessage();
        message.setFrom("dummy-sender");
        message.setTo(mail.getRecipient());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());

        // when
        mailService.sendMail(mail);

        // then
        verify(mailSender, times(1)).send(message);
    }

    @Test
    void sendMail_exceptionTest() {
        // given
        var mail = Mail.builder()
            .recipient("dummy")
            .subject("dummy")
            .message("dummy")
            .build();
        var message = new SimpleMailMessage();
        message.setFrom("dummy-sender");
        message.setTo(mail.getRecipient());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());

        doThrow(new RuntimeException()).when(mailSender).send(message);

        // when
        mailService.sendMail(mail);
    }
}

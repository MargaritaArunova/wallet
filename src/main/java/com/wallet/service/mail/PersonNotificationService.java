package com.wallet.service.mail;

import com.wallet.model.dto.mail.Mail;
import com.wallet.model.dto.mail.NotificationMail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonNotificationService {

    private final MailService mailService;

    public void notifyOnPersonCreate(String recipientEmail) {
        mailService.sendMail(buildMail(recipientEmail, NotificationMail.CREATE));
    }

    private Mail buildMail(String recipient, NotificationMail mail) {
        return Mail.builder()
            .recipient(recipient)
            .subject(mail.getSubject())
            .message(mail.getMessage())
            .build();
    }

}

package com.wallet.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wallet.service.mail.MailService;
import com.wallet.service.mail.PersonNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = PersonNotificationServiceTest.class)
public class PersonNotificationServiceTest {

    private PersonNotificationService personNotificationService;

    @MockBean
    private MailService mailService;

    @BeforeEach
    void setUp() {
        personNotificationService = new PersonNotificationService(
            mailService
        );
    }

    @Test
    void notifyOnPersonCreate_test() {
        // given
        // In setUp

        // when
        personNotificationService.notifyOnPersonCreate(anyString());

        // then
        verify(mailService, times(1)).sendMail(any());
    }
}

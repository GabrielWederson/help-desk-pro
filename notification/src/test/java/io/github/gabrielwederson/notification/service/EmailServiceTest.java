package io.github.gabrielwederson.notification.service;

import io.github.gabrielwederson.notification.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void shouldSendEmailSuccessfully() {

        emailService.sendEmailSync(
                "gabriel@test.com",
                "Subject Test",
                "Body Test"
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals("gabriel@test.com", message.getTo()[0]);
        assertEquals("Subject Test", message.getSubject());
        assertEquals("Body Test", message.getText());
    }

    @Test
    void shouldCallSendEmailSyncWhenUsingAsyncMethod() {

        EmailService spy = spy(emailService);

        doNothing().when(spy)
                .sendEmailSync(anyString(), anyString(), anyString());

        spy.sendEmail(
                "gabriel@test.com",
                "Subject",
                "Body"
        );

        verify(spy).sendEmailSync(
                "gabriel@test.com",
                "Subject",
                "Body"
        );
    }
}

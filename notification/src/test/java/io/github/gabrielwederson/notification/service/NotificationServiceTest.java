package io.github.gabrielwederson.notification.service;

import com.rabbitmq.client.Channel;
import io.github.gabrielwederson.notification.service.email.EmailService;
import io.github.gabrielwederson.notification.service.listener.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private Channel channel;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldAckMessageWhenEmailIsSent() throws Exception {

        MessageProperties properties = new MessageProperties();
        properties.setDeliveryTag(1L);

        Message message = new Message(new byte[0], properties);

        notificationService.ticketComplete(
                "gabriel@test.com",
                message,
                channel
        );

        verify(emailService).sendEmail(
                eq("gabriel@test.com"),
                anyString(),
                anyString()
        );

        verify(channel).basicAck(1L, false);

        verify(channel, never())
                .basicNack(anyLong(), anyBoolean(), anyBoolean());
    }

    @Test
    void shouldNackMessageWhenEmailThrowsException() throws Exception {

        MessageProperties properties = new MessageProperties();
        properties.setDeliveryTag(1L);

        Message message = new Message(new byte[0], properties);

        doThrow(new RuntimeException())
                .when(emailService)
                .sendEmail(anyString(), anyString(), anyString());

        notificationService.ticketComplete(
                "gabriel@test.com",
                message,
                channel
        );

        verify(channel).basicNack(
                1L,
                false,
                false
        );

        verify(channel, never())
                .basicAck(anyLong(), anyBoolean());
    }
}

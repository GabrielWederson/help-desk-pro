package io.github.gabrielwederson.notification.service.listener;

import com.rabbitmq.client.Channel;
import io.github.gabrielwederson.notification.service.email.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService {

    @Autowired
    private EmailService emailService;

    private static String body = "The ticket you submitted has been resolved and updated in the system!!";

    private static String subject = "Ticket complete successfully";

    @RabbitListener(queues = "${rabbit.queue.ticket.complete}")
    public void ticketComplete(String email,
                               Message message,
                               Channel channel) throws IOException {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {

            emailService.sendEmail(email, subject, body);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {

            channel.basicNack(deliveryTag, false, false);
        }
    }

}

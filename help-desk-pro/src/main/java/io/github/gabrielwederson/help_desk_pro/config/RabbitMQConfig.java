package io.github.gabrielwederson.help_desk_pro.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    @Value("${rabbit.ticketcomplete.exchange}")
    private String ticketComplete;

    @Bean
    public Queue queueTicketCompleteMSNotification() {

        return QueueBuilder
                .durable("ticket-complete.ms-notification")
                .deadLetterExchange("")
                .deadLetterRoutingKey(
                        "ticket-complete.ms-notification.retry.1")
                .build();
    }

    @Bean
    public Queue retry1() {

        return QueueBuilder
                .durable("ticket-complete.ms-notification.retry.1")
                .ttl(10000)
                .deadLetterExchange("")
                .deadLetterRoutingKey(
                        "ticket-complete.ms-notification.retry.2")
                .build();
    }

    @Bean
    public Queue retry2() {

        return QueueBuilder
                .durable("ticket-complete.ms-notification.retry.2")
                .ttl(10000)
                .deadLetterExchange("")
                .deadLetterRoutingKey(
                        "ticket-complete.ms-notification.retry.3")
                .build();
    }

    @Bean
    public Queue retry3() {

        return QueueBuilder
                .durable("ticket-complete.ms-notification.retry.3")
                .ttl(10000)
                .deadLetterExchange("")
                .deadLetterRoutingKey(
                        "ticket-complete.ms-notification.dlq")
                .build();
    }

    @Bean
    public Queue dlq() {

        return QueueBuilder
                .durable("ticket-complete.ms-notification.dlq")
                .build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initiateAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange FanoutExchangeTicketComplete(){
        return ExchangeBuilder.fanoutExchange(ticketComplete).build();
    }

    @Bean
    public Binding bindingTicketCompleteMsNotification(){
        return  BindingBuilder.bind(queueTicketCompleteMSNotification())
                .to(FanoutExchangeTicketComplete());
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonmessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonmessageConverter());

        return rabbitTemplate;
    }

}

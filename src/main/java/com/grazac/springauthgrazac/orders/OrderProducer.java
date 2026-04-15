package com.grazac.springauthgrazac.orders;

import com.grazac.springauthgrazac.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }



    public void sendMessage(ProducerMessage request) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE, request.getEmail());
        System.out.println("Sent: " + request.getEmail());
    }
}


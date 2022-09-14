package com.email.CommunicationService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer
{
    @KafkaListener(topics = "CommunicationTopic")
    public void processMessage(String content)
    {
        //Email
        System.out.println("Message Received on topic CommunicationTopic "+content);

    }
}

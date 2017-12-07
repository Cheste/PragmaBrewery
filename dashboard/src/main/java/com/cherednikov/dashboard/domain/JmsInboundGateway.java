package com.cherednikov.dashboard.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * Created by steve on 05/12/17.
 */
@Service
@Log4j2
public class JmsInboundGateway {

    @Value("${ui.topic.temperature}")
    private String topicTemperature;

    @Value("${ui.topic.alert}")
    private String topicAlert;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "${queue.temperature}", containerFactory = "eventBusFactory")
    public void handleTemperatureMessage(TextMessage inboundMessage) {
        Message message = unmarshal(inboundMessage);
        log.info("Temperature received: {}", message);
        messageProducer.emit(topicTemperature, new UIMessage(String.format("%.1f",message.getTemperature()), message.getType()));
    }


    @JmsListener(destination = "${queue.alert}", containerFactory = "eventBusFactory")
    public void handleAlertMessage(TextMessage inboundMessage) {
        Message message = unmarshal(inboundMessage);
        log.info("Emitting alert event: {}", message);
        messageProducer.emit(topicAlert, new UIMessage(String.format("%.1f",message.getTemperature()), message.getType()));
    }


    private Message unmarshal(TextMessage inboundMessage) {
        Message message;
        try {
            message = objectMapper.readValue(inboundMessage.getText(), Message.class);
        } catch (JMSException  | IOException e) {
           throw new IllegalStateException(String.format("Cannot deserialize %s into %s", inboundMessage, Message.class.getSimpleName()), e);
        }
        return message;
    }


}

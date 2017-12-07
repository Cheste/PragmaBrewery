package com.cherednikov.dashboard.domain;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;

/**
 * Created by steve on 05/12/17.
 */
@Service
@Log4j2
public class MessageProducer implements ApplicationListener<BrokerAvailabilityEvent> {

    private SimpMessagingTemplate messagingTemplate;
    private boolean brokerAvailable;

    public MessageProducer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void emit( String destination, UIMessage uiMessage){

        if (brokerAvailable) {
            messagingTemplate.convertAndSend(destination, uiMessage);
        } else {
            log.warn("Broker is not yet available!");
        }
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent brokerAvailabilityEvent) {
        brokerAvailable = brokerAvailabilityEvent.isBrokerAvailable();
    }
}

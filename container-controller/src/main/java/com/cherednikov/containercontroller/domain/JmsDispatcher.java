package com.cherednikov.containercontroller.domain;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Current class dispatch Message to destination queue/topic.
 * Uses thread pool with one thread to keep order of messages. When thread is busy message is placed to LinkedBlockingQueue and processed later.
 */
@Service
@Log4j2
public class JmsDispatcher {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private JmsTemplate jmsTemplate;

    @Setter
    private String destination;

    public JmsDispatcher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = Validate.notNull(jmsTemplate);
    }

    public Future dispatch(Message message){
        return executorService.submit(() -> jmsTemplate.convertAndSend(destination, message));
    }

}

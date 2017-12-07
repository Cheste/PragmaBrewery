package com.cherednikov.containercontroller.infrastructure;

import com.cherednikov.containercontroller.domain.JmsDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by steve on 06/12/17.
 */
@Configuration
public class DomainConfiguration {
    @Value("${queue.temperature}") private String temperatureDestination;
    @Value("${queue.alert}") private String alertDestination;

    @Bean(name = "temperatureDispatcher")
    public JmsDispatcher temperatureDispatcher(JmsTemplate jmsTemplate) {
        JmsDispatcher jmsDispatcher = new JmsDispatcher(jmsTemplate);
        jmsDispatcher.setDestination(temperatureDestination);
        return jmsDispatcher;
    }

    @Bean(name = "alertDispatcher")
    public JmsDispatcher alertDispatcher(JmsTemplate jmsTemplate) {
        JmsDispatcher jmsDispatcher = new JmsDispatcher(jmsTemplate);
        jmsDispatcher.setDestination(alertDestination);
        return jmsDispatcher;
    }

}

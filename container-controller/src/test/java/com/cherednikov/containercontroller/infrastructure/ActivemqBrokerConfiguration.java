package com.cherednikov.containercontroller.infrastructure;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;

/**
 * Created by sncv on 2017-12-06.
 */
@Configuration
@EnableJms
@Import({ActiveMQAutoConfiguration.class, JmsAutoConfiguration.class})
public class ActivemqBrokerConfiguration {

    @Bean
    public DefaultJmsListenerContainerFactory eventBusFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(new SimpleMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        factory.setDestinationResolver((session, destinationName, pubSubDomain) ->  new ActiveMQQueue(destinationName));
        return factory;
    }

}


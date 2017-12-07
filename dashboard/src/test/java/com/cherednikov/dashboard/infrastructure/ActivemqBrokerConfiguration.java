package com.cherednikov.dashboard.infrastructure;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;

/**
 * Created by steve on 06/12/17.
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


    @Bean
    public MessageConverter outboundMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}

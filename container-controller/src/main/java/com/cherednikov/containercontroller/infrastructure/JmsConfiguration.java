package com.cherednikov.containercontroller.infrastructure;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

/**
 * Created by sncv on 2017-12-06.
 */
@Configuration
@EnableJms
public class JmsConfiguration {

    @Value("${jmsbroker.login}") private String jmsLogin;
    @Value("${jmsbroker.password}") private String jmsPassword;
    @Value("${jmsbroker.url}") private String jmsUrl;

    @Bean
    public ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory(jmsLogin, jmsPassword, jmsUrl);
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setMessageConverter(jacksonJmsMessageConverter());
        return template;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}

package com.cherednikov.dashboard.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * Created by steve on 06/12/17.
 */
@Configuration
public class JmsSerializationConfiguration {

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter inboundJmsMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
